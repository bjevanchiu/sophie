//package com.tj.sophie.job.handler;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Scanner;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.tj.sophie.core.AbstractHandler;
//import com.tj.sophie.core.IContext;
//import com.tj.sophie.job.service.Actions;
//
//public class ExecutedHandler extends AbstractHandler {
////	private static Pattern pattern_prepare_kernel_cred = Pattern.compile("符号 prepare_kernel_cred 地址为 (?<prepareKernelCred>(0[Xx])?[0-9A-Fa-f]{8})");
////	private static Pattern pattern_commit_creds = Pattern.compile("符号 commit_creds 地址为 (?<commitCreds>(0[Xx])?[0-9A-Fa-f]{8})");
////	private static Pattern pattern_tty_fasync = Pattern.compile("符号 tty_fasync 地址为 (?<ttyFasync>(0[Xx])?[0-9A-Fa-f]{8})");
////	private static Pattern pattern_ptmx_open = Pattern.compile("符号 ptmx_open 地址为 (?<ptmxOpen>(0[Xx])?[0-9A-Fa-f]{8})");
////	private static Pattern pattern_tty_init_dev = Pattern.compile("符号 tty_init_dev 地址为 (?<ttyInitDev>(0[Xx])?[0-9A-Fa-f]{8})");
////	private static Pattern pattern_tty_release = Pattern.compile("符号 tty_release 地址为 (?<ttyRelease>(0[Xx])?[0-9A-Fa-f]{8})");
////	private static Pattern pattern_ptmx_fops_address = Pattern.compile("符号 ptmx_fops_address 地址为: (?<ptmxFopsAddress>(0[Xx])?[0-9A-Fa-f]{8})");
//	
//	private static Pattern pattern_7 = Pattern.compile("符号 prepare_kernel_cred 地址为 (?<prepareKernelCred>(0[Xx])?[0-9A-Fa-f]{8}).*符号 commit_creds 地址为 (?<commitCreds>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_fasync 地址为 (?<ttyFasync>(0[Xx])?[0-9A-Fa-f]{8}).*符号 ptmx_open 地址为 (?<ptmxOpen>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_init_dev 地址为 (?<ttyInitDev>(0[Xx])?[0-9A-Fa-f]{8}).*符号 tty_release 地址为 (?<ttyRelease>(0[Xx])?[0-9A-Fa-f]{8}).*符号 ptmx_fops_address 地址为: (?<ptmxFopsAddress>(0[Xx])?[0-9A-Fa-f]{8})");
//	private static Pattern pattern_sys_setresuid = Pattern.compile("sys_setresuid 的偏移为: (?<sysSetresuid>(0[Xx])?[0-9A-Fa-f]{8})");
//	private static Pattern pattern_hack_point = Pattern.compile("hack point (?<hackPoint>(0[Xx])?[0-9A-Fa-f]{8})");
//	private static Pattern pattern_shell_code = Pattern.compile("第 (?<shellCode>([1-2])) 个 shellcode 尝试完毕, 执行结果: 1, R结果: 1");
//	private static Pattern pattern_install_status = Pattern.compile("命令正常执行, 返回值: (?<installStatus>(-?\\d))");
//	private static Pattern pattern_fsync_status = Pattern.compile("调用 fsync.* , 返回值 : (?<fsyncStatus>(-?\\d))");
//	private static Pattern pattern_ptmx_return = Pattern.compile("打开 /dev/ptmx , 返回值 : (?<ptmxReturn>(-?\\d))");
//	
////	private static ArrayList<Pattern> pattern_array = new ArrayList<Pattern>();
//	
//	
//
//	@Override
//	protected void onInitialize() {
//		this.setAction(Actions.Executed);
//	}
//
//	@Override
//	protected void onExecute(IContext context) {
//        JsonObject json = context.getVariable("json");
//        if (json == null|| !json.has("eventId") || !json.get("eventId").getAsString().equals("solution_executed") || !json.has("reasons")){
//        	return;
//        }
//        JsonObject reason = json.get("reason").getAsJsonObject();
//        if(reason.get("id") != null){
//        String executed_solution_id = reason.get("id").getAsString();
//        context.setResult("executed_solution_id", executed_solution_id);
//        }
//        if(reason.get("status") != null){
//        	String executed_status = reason.get("status").getAsString();
//        	 context.setResult("executed_status", executed_status);
//        }
//        if(reason.get("log") != null ){
//        	String logStirng = reason.get("log").getAsString();
//        	if(!logStirng.contains("SC 获取已知符号地址 1") && logStirng.contains("调用 fsync() , 返回值 : 0")){//捕获的地址才有意义
//        		getAddresses(logStirng, context);
//        	}
//        	getReasonsLogRestInfo(logStirng, context);//不用捕获地址
//        }
//        return;
//
//	}
//
//	private void getReasonsLogRestInfo(String logStirng, IContext context) {
//		if(logStirng.contains("SC 获取已知符号地址 1")){context.setResult("is_known", new Integer(1));}else{context.setResult("is_known", new Integer(0));}
//		Matcher matcher_shell_code = pattern_shell_code.matcher(logStirng);
//		while(matcher_shell_code.find()){
//			String shell_code = matcher_shell_code.group("shellCode");
//			context.setResult("shell_code", shell_code);
//		}
//		Matcher matcher_install_status = pattern_install_status.matcher(logStirng);
//		while(matcher_install_status.find()){
//			String install_status = matcher_install_status.group("installStatus");
//			int status = Integer.parseInt(install_status);
//			int nut_code = 0x1F & status;
//			int nut_exist = 0x60 & status;
//			context.setResult("nut_code", new Integer(nut_code));
//			context.setResult("nut_exist", new Integer(nut_exist));
//		}
//		Matcher matcher_fsync_status = pattern_fsync_status.matcher(logStirng);
//		while(matcher_fsync_status.find()){
//			String fsync_status = matcher_fsync_status.group("fsyncStatus");
//			context.setResult("fsync_status", fsync_status);
//		}
//		Matcher mather_ptmx_return = pattern_ptmx_return.matcher(logStirng);
//		while(mather_ptmx_return.find()){
//			String ptmx_return = mather_ptmx_return.group("ptmxReturn");
//			context.setResult("ptmx_return", ptmx_return);
//		}
//		
//		
//	}
//
//	private void getAddresses(String logString, IContext context) {
//		
//		Matcher matcher_7 = pattern_7.matcher(logString);
//		while (matcher_7.find()) {
//			String prepare_kernel_cred = matcher_7.group("prepareKernelCred");
//			String commit_creds = matcher_7.group("commitCreds");
//			String tty_fasync = matcher_7.group("ttyFasync");
//			String ptmx_open = matcher_7.group("ptmxOpen");
//			String tty_init_dev = matcher_7.group("ttyInitDev");
//			String tty_release = matcher_7.group("ttyRelease");
//			String ptmx_fops_address = matcher_7.group("ptmxFopsAddress");
//			context.setResult("prepare_kernel_cred", prepare_kernel_cred);
//			context.setResult("commit_creds", commit_creds);
//			context.setResult("tty_fasync", tty_fasync);
//			context.setResult("ptmx_open", ptmx_open);
//			context.setResult("tty_init_dev", tty_init_dev);
//			context.setResult("tty_release", tty_release);
//			context.setResult("ptmx_fops_address", ptmx_fops_address);
//        }
//		Matcher matcher_sys_setresuid = pattern_sys_setresuid.matcher(logString);
//		while(matcher_sys_setresuid.find()){
//			String sys_setresuid = matcher_sys_setresuid.group("sysSetresuid");
//			context.setResult("sys_setresuid", sys_setresuid);
//		}
//		Matcher matcher_hack_point = pattern_hack_point.matcher(logString);
//		while(matcher_hack_point.find()){
//			String hack_point = matcher_hack_point.group("hackPoint");
//			context.setResult("hack_point", hack_point);
//		}
//		
//	
//		
//		
//
//		
//	}
//	
//
//}
