package com.inverted.tech.mission2048.common;

/**
 * Callback interface for authorization events.
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public interface AuthListener {

	public void onAuthSucceed();

	public void onAuthFail(String error);
}
