package com.kaisengao.version;

/**
 * @ClassName: KsgVersionConfig
 * @Author: KaiSenGao
 * @CreateDate: 2020/11/23 10:22
 * @Description: 版本配置
 */
public class KsgVersionConfig {

    private String mApkName;

    private String mDownloadUrl;

    private String mVersionInfo;

    private int mNewVersionCode;

    private boolean isForceUp;

    private boolean mJumpInstall;

    private boolean isShowNotification;

    public String getApkName() {
        return mApkName;
    }

    public void setApkName(String apkName) {
        mApkName = apkName;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public String getVersionInfo() {
        return mVersionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        mVersionInfo = versionInfo;
    }

    public int getNewVersionCode() {
        return mNewVersionCode;
    }

    public void setNewVersionCode(int newVersionCode) {
        mNewVersionCode = newVersionCode;
    }

    public boolean isForceUp() {
        return isForceUp;
    }

    public void setForceUp(boolean forceUp) {
        isForceUp = forceUp;
    }

    public boolean isJumpInstall() {
        return mJumpInstall;
    }

    public void setJumpInstall(boolean jumpInstall) {
        mJumpInstall = jumpInstall;
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    public void setShowNotification(boolean showNotification) {
        isShowNotification = showNotification;
    }
}