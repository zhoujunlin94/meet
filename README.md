# meet

#### 介绍

工作学习代码idea小合集

doc-开发环境搭建文档积累

如何将自己开发的jar上传到maven中央仓库？ 参考：https://juejin.cn/post/7347207466818289703

打包 mvn clean deploy -Dgpg.passphrase=密码

mvn clean deploy -pl meet-framework/meet-common -DskipTests "-Dgpg.passphrase=密码"