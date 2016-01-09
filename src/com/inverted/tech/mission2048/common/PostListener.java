package com.inverted.tech.mission2048.common;

/**
 * Callback interface for post events.
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public interface PostListener {

	public void onPostPublished();

	public void onPostPublishingFailed();
}