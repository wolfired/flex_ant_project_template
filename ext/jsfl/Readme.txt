一. 设置
1. 把本目录下的jsfl脚本放到以下路径:

	Flash CS5.5:
		C:\Users\Administrator\AppData\Local\Adobe\Flash CS5.5\zh_CN\Configuration\Commands\

	Flash CS6:
		C:\Users\Administrator\AppData\Local\Adobe\Flash CS6\zh_CN\Configuration\Commands\

2. 在Flash CS的<命令>菜单下可以使用对应脚本

二. 用法：

1. 新建一个fla, 命名为uitest.fla

2. 执行<构造目录结构.jsfl>, 生成以下目录结构

	test
	 ├─btn
	 ├─img
	 ├─mc
	 ├─output
	 └─custom

	btn: 放置 按钮
	img: 放置 原始美术资源
	mc: 放置 影片剪辑 和 图形
	output: 要导出到as的皮肤, 包名前缀为test, 例如目录下有个皮肤OutputWindow, 皮肤的as类链接为: test.skin.OutputWindow
	custom: 要导出到as的皮肤, 包名前缀为custom, 例如目录下有个皮肤CustomWindow, 皮肤的as类链接为: custom.skin.CustomWindow

3. 执行<定义外部组件.jsfl>, 提示指定一个根目录, 默认直接点确定, 定义output和custom目录下所有皮肤的as链接

4. 按预设规则打印一个影片剪辑里所有已命名元件

	a. 新建一个影片剪辑
	b. 编辑影片剪辑, 新建一个文本元件并命名为_txt_name
	c. 执行<打印属性名.jsfl>会输出as代码
	d. 把as代码复制到相应的源码里即可

5. 创建一个可拉伸的影片剪辑

	a. 新建一个影片剪辑
	b. 右键影片剪辑, 选属性, 勾选中"启用 9 切片绽放比例辅助线"
	c. 编辑影片剪辑, 放入一张图片, 把"辅助线"调整到适当位置, 选中图片, 执行<BitmapSlice9.jsfl>

6. 创建一个有很多帧的影片剪辑

	a. 新建一个影片剪辑
	b. 编辑影片剪辑, 放入多张图片, 根据需求把图片按X轴/Y轴排序
	c. 执行<分散到帧.jsfl>即可把图片分散插入到不同的帧