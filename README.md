# ZsmUpdateX

[![](https://jitpack.io/v/zhengshaomin/ZsmUpdateX.svg)](https://jitpack.io/#zhengshaomin/ZsmUpdateX)

## Function description(功能描述)：

ZsmUpdateX旨在简化应用程序的自动更新和安装流程。通过 ZsmUpdateX，开发人员只需传入三个必要参数，即更新文件的标题、内容和下载链接，即可实现自动下载更新文件并在下载完成后自动安装。该库提供了一个简单的接口，开发人员可以轻松地将更新集成到他们的应用程序中，无需处理复杂的下载和安装逻辑。ZsmUpdateX 使用了最新的安全性和稳定性技术，确保用户能够安全地获取和安装更新。

#### ZsmUpdateX 的主要特点包括：： 

##### -简单易用：只需传入三个必要参数即可完成自动更新和安装。

##### -自动安装：下载完成后自动触发安装流程，用户无需手动安装更新文件。

##### -安全可靠：使用最新的安全性技术，确保用户获取和安装的更新文件是安全可靠的。

#### 通过 ZsmUpdateX，开发人员可以轻松地为他们的应用程序提供自动更新功能，提升用户体验，并保持应用程序的最新版本。

### 使用方法

__Step 1.__ 在你的根 build.gradle 文件末尾添加它到 repositories 中：

```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

__Step 2.__ 添加依赖

 ```
dependencies {
	        implementation 'com.github.zhengshaomin:ZsmUpdateX:1.0.2'
	}
```

__Step 3.__ 初始化

 ```
ZsmUpdateX.initialize(MainActivity.this,"cn.zsmupdatex.myapplication",
                new ZsmUpdateX.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //初始化成功
                    }
                },
                new ZsmUpdateX.ErrorCallback() {
                    @Override
                    public void onError(String errorMessage) {
                        Log.d(TAG,errorMessage);
                    }
                }
        );
```

__Step 4.__ 传入必要参数

```
ZsmUpdateX zsmUpdateX=new ZsmUpdateX();
	zsmUpdateX.setTitle("")
		.setContent("")
		.setUrl("")
		.start(this);
```

