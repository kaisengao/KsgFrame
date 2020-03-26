# KsgFrame
一些开发常用的模式，工具，自定义等

#### lib_base
此依赖包主要用于项目中常用的一些基本的三方库，工具类，自定义等

### lib_mvp
此依赖包主要是MVP模式，此包内部分别有Java代码版本与Kotlin代码版本
该MVP模式带有全局添加Toolbar功能

## Java版本
```java
  @Override
    public void setContentView(@LayoutRes int layoutResId) {
        if (layoutResId == 0) {
            return;
        }
        if (this.isDisplayToolbar()) {
            this.initContentView(layoutResId);
        } else {
            super.setContentView(layoutResId);
        }
    }

    /**
     * 将Toolbar布局添加到容器中
     *
     * @param layoutResId 内容布局Id
     */
    private void initContentView(@LayoutRes int layoutResId) {
        // 获取Ac父容器content
        ViewGroup viewGroup = this.findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        // 创建一个垂直线性布局
        this.mParentLinearLayout = new LinearLayout(this);
        this.mParentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        // 将线性布局添加入父容器中，作为Ac页面布局的父容器
        viewGroup.addView(this.mParentLinearLayout);
        // 将Toolbar添加到父容器布局中
        this.getLayoutInflater().inflate(getToolbarLayoutId(), mParentLinearLayout);
        if (this.mParentLinearLayout.getChildCount() > 0) {
            // 沉浸式状态栏 添加padding高度
            StatusBarUtil.setPaddingSmart(this, this.mParentLinearLayout.getChildAt(0));
        } else {
            // 如果没有添加Toolbar就移除沉浸式效果
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 将ContentLayout添加到父容器布局中
        this.getLayoutInflater().inflate(layoutResId, this.mParentLinearLayout);
        // 获取ContentLayout的View 以作为LoadSir的注册布局
        this.mContentLayout = this.mParentLinearLayout.getChildAt(1);
    }

    /**
     * 子类Activity重写该方法可以设置是否显示Toolbar
     *
     * @return 默认返回true表示显示Toolbar，如不需要Toolbar，则重写该方法返回false
     */
    protected boolean isDisplayToolbar() {
        return true;
    }

    /**
     * 得到当前界面Toolbar的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected int getToolbarLayoutId() {
        return R.layout.toolbar;
    }
 ```
 ## Kotlin版本
 ```kotin
  override fun setContentView(@LayoutRes layoutResId: Int) {
        if (layoutResId == 0) {
            return
        }
        if (this.isDisplayToolbar()) {
            this.initContentView(layoutResId)
        } else {
            super.setContentView(layoutResId)
        }
    }

    /**
     * 将Toolbar布局添加到容器中
     *
     * @param layoutResId 内容布局Id
     */
    private fun initContentView(@LayoutRes layoutResId: Int) {
        // 获取Ac父容器content
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        viewGroup.removeAllViews()
        // 创建一个垂直线性布局
        this.mParentLinearLayout = LinearLayout(this)
        this.mParentLinearLayout!!.orientation = LinearLayout.VERTICAL
        // 将线性布局添加入父容器中，作为Ac页面布局的父容器
        viewGroup.addView(this.mParentLinearLayout)
        // 将Toolbar添加到父容器布局中
        this.layoutInflater.inflate(getToolbarLayoutId(), this.mParentLinearLayout)
        if (this.mParentLinearLayout!!.childCount > 0) { // 沉浸式状态栏 添加padding高度
            StatusBarUtil.setPaddingSmart(this, this.mParentLinearLayout!!.getChildAt(0))
        } else { // 如果没有添加Toolbar就移除沉浸式效果
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        // 将ContentLayout添加到父容器布局中
        this.layoutInflater.inflate(layoutResId, this.mParentLinearLayout)
        // 获取ContentLayout的View 以作为LoadSir的注册布局
        this.mContentLayout = this.mParentLinearLayout!!.getChildAt(1)
    }

    /**
     * 子类Activity重写该方法可以设置是否显示Toolbar
     *
     * @return 默认返回true表示显示Toolbar，如不需要Toolbar，则重写该方法返回false
     */
    protected open fun isDisplayToolbar(): Boolean = true

    /**
     * 得到当前界面Toolbar的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected open fun getToolbarLayoutId(): Int = R.layout.toolbar
```
