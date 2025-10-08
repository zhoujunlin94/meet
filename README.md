# meet

#### 介绍

工作学习代码idea小合集

doc-开发环境搭建文档积累

一键更新版本号

1. mvn versions:set "-DnewVersion=1.2.3"
2. 直接修改父pom revision （推荐）

如何将自己开发的jar上传到maven中央仓库？ 参考：https://juejin.cn/post/7347207466818289703

1. 手动发布 打包 mvn clean deploy -DskipTests "-Dgpg.passphrase=密码"
   mvn clean deploy -pl meet-framework/meet-common -DskipTests "-Dgpg.passphrase=密码"

2. 自动发布 github action 每次修改maven_settings.xml中的用户+密码即可

