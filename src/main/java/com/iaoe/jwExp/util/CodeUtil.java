package com.iaoe.jwExp.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于对比验证码的正确性
 * @author iAoe
 *
 */
public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//获取真实的验证码
		String verifyCodeExpected = (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		verifyCodeExpected = verifyCodeExpected.toLowerCase();
		//获取实际填写的验证码
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual").toLowerCase();
		if(verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
