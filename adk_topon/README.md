双端都未上架的可以接：mtg, unity, topon adx

|目录/文件|说明|
|:--|:--|
|libs| 聚合第三方Network SDK必须集成的包目录(将里面的aar和jar放置开发工程的libs目录下)|
|build.gradle|项目gradle需要集成的代码内容|
|AndroidManifest.xml|需将AndroidManifest里的组件和配置信息添加到开发工程的|AndroidManifest（文件不存在则不需要配置）|
|proguard-android.txt|混淆配置（文件不存在则不需要配置）|
|res|聚合第三方Network SDK必须导入的资源（有则将里面所有的文件夹复制到工程的res目录下，没有则不需要导入）|
|keep.xml|如果打包时有开启shrinkResources或者接入了部分第三方的资源优化框架（如：AndResProguard），则必须将此文件中的资源添加到白名单，否则将导致崩溃或者广告异常|