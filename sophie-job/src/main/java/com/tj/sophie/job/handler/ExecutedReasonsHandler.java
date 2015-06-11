package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.helper.Helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/10/15.
 */
@Handler
public class ExecutedReasonsHandler extends AbstractHandler {

    private Pattern supportPattern = Pattern.compile("\\\\n\\d+:info:\\\\t读写器在此机器上是否支持 *(?<support>\\d+) *\\.");
    private Pattern pattern_7 = Pattern.compile("符号 prepare_kernel_cred 地址为 (?<prepareKernelCred>(0[Xx])?[0-9A-Fa-f]{8}).*符号 commit_creds 地址为 (?<commitCreds>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_fasync 地址为 (?<ttyFasync>(0[Xx])?[0-9A-Fa-f]{8}).*符号 ptmx_open 地址为 (?<ptmxOpen>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_init_dev 地址为 (?<ttyInitDev>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_release 地址为 (?<ttyRelease>(0[Xx])?[0-9A-Fa-f]{8}).*符号 ptmx_fops_address 地址为: (?<ptmxFopsAddress>(0[Xx])?[0-9A-Fa-f]{8})");
	private Pattern pattern_sys_setresuid = Pattern.compile("sys_setresuid 的偏移为: (?<sysSetresuid>(0[Xx])?[0-9A-Fa-f]{8})");
	private Pattern pattern_hack_point = Pattern.compile("hack point (?<hackPoint>(0[Xx])?[0-9A-Fa-f]{8})");
	private Pattern pattern_shell_code = Pattern.compile("第 (?<shellCode>([1-2])) 个 shellcode 尝试完毕, 执行结果: 1, R结果: 1");
	private Pattern pattern_install_status = Pattern.compile("命令正常执行, 返回值: (?<installStatus>(-?\\d))");
	private Pattern pattern_fsync_status = Pattern.compile("调用 fsync.* , 返回值 : (?<fsyncStatus>(-?\\d))");
	private Pattern pattern_ptmx_return = Pattern.compile("打开 /dev/ptmx , 返回值 : (?<ptmxReturn>(-?\\d))");
    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessReasons);
    }

    @Override
    protected void onExecute(IContext context) {
        String event = context.getVariable("event");
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("solution_executed")) {
            return;
        }
        JsonObject reasons = context.getVariable("reasons_json");
        if (!reasons.has("id")
                || !reasons.has("exit_id")
                || !reasons.has("core_log")
                || !reasons.has("status")
                || !reasons.has("times")
                || !reasons.has("log")
                || !reasons.has("message")) {
            context.setError("reasons", reasons);
        }
        context.getMap("result").put("solution_id", reasons.get("id").getAsString());
        context.getMap("result").put("exit_id", reasons.get("exit_id").getAsString());
        context.getMap("result").put("status", reasons.get("status").getAsString());
        context.getMap("result").put("times", reasons.get("times").getAsString());
        context.getMap("result").put("message", reasons.get("message").getAsString());
        String log = reasons.get("log").getAsString();
        String status = reasons.get("status").getAsString();
        Matcher matcher = supportPattern.matcher(log);
        String support = null;
        while (matcher.find()) {
        	support = matcher.group("support");
        }
        if ("fail".equalsIgnoreCase(status)) {
        	if ("0".equalsIgnoreCase(support)) {
        		context.getMap("result").put("solution_support", "false");
        		return;
        	}
        }
        if ("1".equalsIgnoreCase(support)) {
    		context.getMap("result").put("solution_support", "true");
    	}
        getLogInfo(log, context);
    }

	private void getLogInfo(String logString, IContext context) {
    	if(!logString.contains("SC 获取已知符号地址 1") && logString.contains("调用 fsync() , 返回值 : 0")){
    		getAddresses(logString, context);
    	}
    	getReasonsLogRestInfo(logString, context);
		
	}
	
	private void getAddresses(String logString, IContext context) {
		
		Matcher matcher_7 = pattern_7.matcher(logString);
		while (matcher_7.find()) {
			String prepare_kernel_cred = matcher_7.group("prepareKernelCred");
			String commit_creds = matcher_7.group("commitCreds");
			String tty_fasync = matcher_7.group("ttyFasync");
			String ptmx_open = matcher_7.group("ptmxOpen");
			String tty_init_dev = matcher_7.group("ttyInitDev");
			String tty_release = matcher_7.group("ttyRelease");
			String ptmx_fops_address = matcher_7.group("ptmxFopsAddress");
			context.getMap("result").put("prepare_kernel_cred", prepare_kernel_cred);
			context.getMap("result").put("commit_creds", commit_creds);
			context.getMap("result").put("tty_fasync", tty_fasync);
			context.getMap("result").put("ptmx_open", ptmx_open);
			context.getMap("result").put("tty_init_dev", tty_init_dev);
			context.getMap("result").put("tty_release", tty_release);
			context.getMap("result").put("ptmx_fops_address", ptmx_fops_address);
        }
		Matcher matcher_sys_setresuid = pattern_sys_setresuid.matcher(logString);
		while(matcher_sys_setresuid.find()){
			String sys_setresuid = matcher_sys_setresuid.group("sysSetresuid");
			context.getMap("result").put("sys_setresuid", sys_setresuid);
		}
		Matcher matcher_hack_point = pattern_hack_point.matcher(logString);
		while(matcher_hack_point.find()){
			String hack_point = matcher_hack_point.group("hackPoint");
			context.getMap("result").put("hack_point", hack_point);
		}
	}
	
	private void getReasonsLogRestInfo(String logStirng, IContext context) {
		if(logStirng.contains("SC 获取已知符号地址 1")){context.getMap("result").put("is_known", new Integer(1));}else{context.getMap("result").put("is_known", new Integer(0));}
		Matcher matcher_shell_code = pattern_shell_code.matcher(logStirng);
		while(matcher_shell_code.find()){
			String shell_code = matcher_shell_code.group("shellCode");
			context.getMap("result").put("shell_code", shell_code);
		}
		Matcher matcher_install_status = pattern_install_status.matcher(logStirng);
		while(matcher_install_status.find()){
			String install_status = matcher_install_status.group("installStatus");
			int status = Integer.parseInt(install_status);
			int nut_code = 0x1F & status;
			int nut_exist = 0x60 & status;
			context.getMap("result").put("nut_code", new Integer(nut_code));
			context.getMap("result").put("nut_exist", new Integer(nut_exist));
		}
		Matcher matcher_fsync_status = pattern_fsync_status.matcher(logStirng);
		while(matcher_fsync_status.find()){
			String fsync_status = matcher_fsync_status.group("fsyncStatus");
			context.getMap("result").put("fsync_status", fsync_status);
		}
		Matcher mather_ptmx_return = pattern_ptmx_return.matcher(logStirng);
		while(mather_ptmx_return.find()){
			String ptmx_return = mather_ptmx_return.group("ptmxReturn");
			context.getMap("result").put("ptmx_return", ptmx_return);
		}
		
		
	}

}
