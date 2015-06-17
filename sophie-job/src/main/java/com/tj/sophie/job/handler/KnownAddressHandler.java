package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.Constants;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/17/15.
 */
public class KnownAddressHandler extends AbstractHandler {
    private Set<String> solution_ids = new HashSet<String>(Arrays.asList("100", "101", "102", "103"));

    private static Pattern pattern_prepare_kernel_cred = Pattern.compile("符号 prepare_kernel_cred 地址为 (?<prepareKernelCred>(0[Xx])?[0-9A-Fa-f]{8})");
    private static Pattern pattern_commit_creds = Pattern.compile("符号 commit_creds 地址为 (?<commitCreds>(0[Xx])?[0-9A-Fa-f]{8})");
    private static Pattern pattern_tty_fasync = Pattern.compile("符号 tty_fasync 地址为 (?<ttyFasync>(0[Xx])?[0-9A-Fa-f]{8})");
    private static Pattern pattern_ptmx_open = Pattern.compile("符号 ptmx_open 地址为 (?<ptmxOpen>(0[Xx])?[0-9A-Fa-f]{8})");
    private static Pattern pattern_tty_init_dev = Pattern.compile("符号 tty_init_dev 地址为 (?<ttyInitDev>(0[Xx])?[0-9A-Fa-f]{8})");
    private static Pattern pattern_tty_release = Pattern.compile("符号 tty_release 地址为 (?<ttyRelease>(0[Xx])?[0-9A-Fa-f]{8})");
    private static Pattern pattern_ptmx_fops_address = Pattern.compile("符号 ptmx_fops_address 地址为: (?<ptmxFopsAddress>(0[Xx])?[0-9A-Fa-f]{8})");

    private Pattern supportPattern = Pattern.compile("\\\\n\\d+:info:\\\\t读写器在此机器上是否支持 *(?<support>\\d+) *\\.");
    //	private Pattern pattern_7 = Pattern.compile("符号 prepare_kernel_cred 地址为 (?<prepareKernelCred>(0[Xx])?[0-9A-Fa-f]{8}).*符号 commit_creds 地址为 (?<commitCreds>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_fasync 地址为 (?<ttyFasync>(0[Xx])?[0-9A-Fa-f]{8}).*符号 ptmx_open 地址为 (?<ptmxOpen>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_init_dev 地址为 (?<ttyInitDev>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_release 地址为 (?<ttyRelease>(0[Xx])?[0-9A-Fa-f]{8}).*符号 ptmx_fops_address 地址为: (?<ptmxFopsAddress>(0[Xx])?[0-9A-Fa-f]{8})");
    private Pattern pattern_sys_setresuid = Pattern.compile("sys_setresuid 的偏移为: (?<sysSetresuid>(0[Xx])?[0-9A-Fa-f]{8})");
    private Pattern pattern_hack_point = Pattern.compile("hack point (?<hackPoint>(0[Xx])?[0-9A-Fa-f]{8})");

    private Pattern pattern_shell_code = Pattern.compile("第 (?<shellCode>([1-2])) 个 shellcode 尝试完毕, .* R结果: ");

    private Pattern pattern_r_result = Pattern.compile("第 \\d 个 shellcode 尝试完毕, .* R结果: (?<rResult>(\\d))");

    private Pattern pattern_install_status = Pattern.compile("命令正常执行, 返回值: (?<installStatus>(-?\\d))");
    private Pattern pattern_fsync_status = Pattern.compile("调用 fsync.* , 返回值 : (?<fsyncStatus>(-?\\d))");
    private Pattern pattern_ptmx_return = Pattern.compile("打开 /dev/ptmx , 返回值 : (?<ptmxReturn>(-?\\d))");
    private Pattern pattern_section1 = Pattern.compile("正在尝试第 1 个 shellcode .*?第 1 个 shellcode 尝试完毕, ");
    private Pattern pattern_section2 = Pattern.compile("正在尝试第 2 个 shellcode .*?第 2 个 shellcode 尝试完毕, ");

    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessKnownRootAddress);
    }

    @Override
    protected void onExecute(IContext context) {


        JsonObject reasons = context.getVariable(Constants.Variables.REASONS);
        JsonObject executed = context.getVariable(Constants.Variables.EXECUTED);

        String log = reasons.get("log").getAsString();
        String status = reasons.get("status").getAsString();
        if (log.contains("SC 获取已知符号地址 1")) {
            context.getMap("result").put("is_known", new Integer(1));
        } else {
            context.getMap("result").put("is_known", new Integer(0));
        }
        Matcher matcher = supportPattern.matcher(log);
        String support = null;
        while (matcher.find()) {
            support = matcher.group("support");
        }
        String solution_id = reasons.get("id").getAsString();
        if (!solution_ids.contains(solution_id)) {
            context.setError("log", context.getInput());
            return;
        }
        if ("0".equalsIgnoreCase(support)) {
            context.getMap("result").put("solution_support", "false");
            if ("Success".equalsIgnoreCase(status)) {
                context.setError("solution_support", context.getInput());
            }
            return;
        }
        List<String> sections = new ArrayList<String>();
        Matcher matcher_section1 = pattern_section1.matcher(log);
        while (matcher_section1.find()) {
            String section1 = matcher_section1.group();
            sections.add(section1);
        }
        Matcher matcher_section2 = pattern_section2.matcher(log);
        while (matcher_section2.find()) {
            String section2 = matcher_section2.group();
            sections.add(section2);
        }
        if (sections.size() == 0) {
            context.setError("sections", context.getInput());
            return;
        }
        Iterator<String> its = sections.iterator();
        while (its.hasNext()) {
            String section = its.next();
            String r_result = null;
            Matcher matcher_r_result = pattern_r_result.matcher(section);
            while (matcher_r_result.find()) {
                r_result = matcher_r_result.group("rResult");
                if ("Success".equalsIgnoreCase(status) && "0".equalsIgnoreCase(r_result)) {
                    context.setError("sections", context.getInput());
                    return;
                }
            }
            Matcher matcher_shell_code = pattern_shell_code.matcher(section);
            while (matcher_shell_code.find()) {
                String shell_code = matcher_shell_code.group("shellCode");
                if ("1".equalsIgnoreCase(shell_code)) {
                    context.setVariable("r_result_8", r_result);
                    context.setVariable("shell_code_8", new Integer(0));
                    if (!section.contains("SC 获取已知符号地址 1")) {
                        getAddresses8(section, context);
                    }
                    getLogRestInfo8(section, context);
                }
                if ("2".equalsIgnoreCase(shell_code)) {
                    context.setVariable("r_result_2", r_result);
                    context.setVariable("shell_code_2", new Integer(0));
                    if (!section.contains("SC 获取已知符号地址 1")) {
                        getAddresses2(section, context);
                    }
                    getLogRestInfo2(section, context);
                }
            }//shell_code
        }//sections
    }

    private void getLogRestInfo2(String section, IContext context) {
        Matcher matcher_install_status = pattern_install_status.matcher(section);
        while (matcher_install_status.find()) {
            String install_status = matcher_install_status.group("installStatus");
            int status = Integer.parseInt(install_status);
            int nut_code = 0x1F & status;
            int nut_exist = 0x60 & status;
            context.setVariable("nut_code_2", new Integer(nut_code));
            context.setVariable("nut_exist_2", new Integer(nut_exist));
        }
        Matcher matcher_fsync_status = pattern_fsync_status.matcher(section);
        while (matcher_fsync_status.find()) {
            String fsync_status = matcher_fsync_status.group("fsyncStatus");
            context.setVariable("fsync_status_2", fsync_status);
        }
        Matcher mather_ptmx_return = pattern_ptmx_return.matcher(section);
        while (mather_ptmx_return.find()) {
            String ptmx_return = mather_ptmx_return.group("ptmxReturn");
            context.setVariable("ptmx_return_2", ptmx_return);
        }

    }

    private void getLogRestInfo8(String section, IContext context) {
        Matcher matcher_install_status = pattern_install_status.matcher(section);
        while (matcher_install_status.find()) {
            String install_status = matcher_install_status.group("installStatus");
            int status = Integer.parseInt(install_status);
            int nut_code = 0x1F & status;
            int nut_exist = 0x60 & status;
            context.setVariable("nut_code_8", new Integer(nut_code));
            context.setVariable("nut_exist_8", new Integer(nut_exist));
        }
        Matcher matcher_fsync_status = pattern_fsync_status.matcher(section);
        while (matcher_fsync_status.find()) {
            String fsync_status = matcher_fsync_status.group("fsyncStatus");
            context.setVariable("fsync_status_8", fsync_status);
        }
        Matcher mather_ptmx_return = pattern_ptmx_return.matcher(section);
        while (mather_ptmx_return.find()) {
            String ptmx_return = mather_ptmx_return.group("ptmxReturn");
            context.setVariable("ptmx_return_8", ptmx_return);
        }

    }

    private void getAddresses2(String section, IContext context) {
        Matcher matcher_sys_setresuid = pattern_sys_setresuid.matcher(section);
        while (matcher_sys_setresuid.find()) {
            String sys_setresuid = matcher_sys_setresuid.group("sysSetresuid");
            context.setVariable("sys_setresuid", sys_setresuid);
        }
        Matcher matcher_hack_point = pattern_hack_point.matcher(section);
        while (matcher_hack_point.find()) {
            String hack_point = matcher_hack_point.group("hackPoint");
            context.setVariable("hack_point_2", hack_point);
        }
    }

    private void getAddresses8(String section, IContext context) {
        Matcher matcher_pattern_prepare_kernel_cred = pattern_prepare_kernel_cred.matcher(section);
        while (matcher_pattern_prepare_kernel_cred.find()) {
            String prepare_kernel_cred = matcher_pattern_prepare_kernel_cred.group("prepareKernelCred");
            context.setVariable("prepare_kernel_cred", prepare_kernel_cred);
        }
        Matcher matcher_pattern_commit_creds = pattern_commit_creds.matcher(section);
        while (matcher_pattern_commit_creds.find()) {
            String commit_creds = matcher_pattern_commit_creds.group("commitCreds");
            context.setVariable("commit_creds", commit_creds);
        }
        Matcher matcher_pattern_tty_fasync = pattern_tty_fasync.matcher(section);
        while (matcher_pattern_tty_fasync.find()) {
            String tty_fasync = matcher_pattern_tty_fasync.group("ttyFasync");
            context.setVariable("tty_fasync", tty_fasync);
        }
        Matcher matcher_pattern_ptmx_open = pattern_ptmx_open.matcher(section);
        while (matcher_pattern_ptmx_open.find()) {
            String ptmx_open = matcher_pattern_ptmx_open.group("ptmxOpen");
            context.setVariable("ptmx_open", ptmx_open);
        }
        Matcher matcher_pattern_tty_init_dev = pattern_tty_init_dev.matcher(section);
        while (matcher_pattern_tty_init_dev.find()) {
            String tty_init_dev = matcher_pattern_tty_init_dev.group("ttyInitDev");
            context.setVariable("tty_init_dev", tty_init_dev);
        }
        Matcher matcher_pattern_tty_release = pattern_tty_release.matcher(section);
        while (matcher_pattern_tty_release.find()) {
            String tty_release = matcher_pattern_tty_release.group("ttyRelease");
            context.setVariable("tty_release", tty_release);
        }
        Matcher matcher_pattern_ptmx_fops_address = pattern_ptmx_fops_address.matcher(section);
        while (matcher_pattern_ptmx_fops_address.find()) {
            String ptmx_fops_address = matcher_pattern_ptmx_fops_address.group("ptmxFopsAddress");
            context.setVariable("ptmx_fops_address", ptmx_fops_address);
        }
        Matcher matcher_hack_point = pattern_hack_point.matcher(section);
        while (matcher_hack_point.find()) {
            String hack_point = matcher_hack_point.group("hackPoint");
            context.setVariable("hack_point_8", hack_point);
        }
    }
}
