# ![](http://www.joefgame.com/imgs/personal.png) [JOEF · GAME](http://www.joefgame.com)
![](https://img.shields.io/badge/Status-%E8%BF%90%E8%A1%8C%E4%B8%AD-green?style=for-the-badge&logo=appveyor)
---

#### ![](http://www.joefgame.com/imgs/find.png) Indie Gamer Community 独立游戏玩家社区
![](https://img.shields.io/badge/-%E7%8B%AC%E7%AB%8B%E6%B8%B8%E6%88%8F-red?style=social&logo=appveyor)  
![](https://img.shields.io/badge/-Steam%E6%95%B0%E6%8D%AE%E5%BA%93-orange?style=social&logo=appveyor)  
![](https://img.shields.io/badge/-%E7%A4%BE%E5%8C%BA-yellow?style=social&logo=appveyor)  
---
### ![](http://www.joefgame.com/imgs/search-pix.png) 网站架构  
![](https://img.shields.io/badge/%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%AF%AD%E8%A8%80-Java-orange?style=flat&logo=appveyor)
![](https://img.shields.io/badge/%E6%9C%8D%E5%8A%A1%E6%8F%90%E4%BE%9B-Nginx/1.17.9-blue?style=flat&logo=appveyor)
---
**基本框架**：SpringBoot + MyBatis  
**前端**：Vue + Html5  
**三方API调用**：Github API，Steam API，UCloud API  
**数据库版本管理**：Flyway    
**缓存提供**：Redis  
**全文检索**：Solr    
**认证、授权**：Shiro  
**流程管理**：Activiti6  
**爬虫**：OkHttp3 + Jsoup  
**其他**：Opencv、OpenID  

---

### ![](http://www.joefgame.com/imgs/notify.png) 模块划分
[持久层（dao）模块](https://github.com/Joezeo/JOEF-GAME/tree/master/JoefGame-dao)：与数据库交互，提供POJO对象，MyBatis Mapper  
[公共（common）模块](https://github.com/Joezeo/JOEF-GAME/tree/master/JoefGame-common)：提供公用DTO类，Enum，工具类...  
[后台管理（manager）模块](https://github.com/Joezeo/JOEF-GAME/tree/master/JoefGame-manager)：网站后台管理系统，需求管理员权限，提供爬虫管理、流程管理等功能  
[前台（potal）模块](https://github.com/Joezeo/JOEF-GAME/tree/master/JoefGame-potal)：网站主要用户页面的服务器模块  
[WEB 模块](https://github.com/Joezeo/JOEF-GAME/tree/master/JoefGame-web)：程序入口，提供Html5页面、JS、CSS、图片等静态资源，SpringBoot Configuration，异常处理  
```
web -
    │
    └─── potal ─────
    |               |
    |               └─── common ─── dao
    |               |
    └─── manager ───
```


