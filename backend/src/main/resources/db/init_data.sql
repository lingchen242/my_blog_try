-- ============================================================
-- init_data.sql - 博客系统数据初始化
-- 包含：用户、个人介绍、博客文章、留言
-- 使用：mysql -u root -p blog < init_data.sql
-- ============================================================

-- ==================== 用户表 ====================
-- 密码统一为 123456，使用 BCrypt 加密
INSERT IGNORE INTO `user` (id, username, password, role, nickname, create_time) VALUES
(1, 'admin',
 '$2a$10$pNNUipgBGxzFo9xJS/zhM.QhQJDA64smO2BUYCXDKzSJNW6GK2Rre',
 'admin', '管理员', NOW());

INSERT IGNORE INTO `user` (id, username, password, role, nickname, create_time) VALUES
(2, 'user',
 '$2a$10$pNNUipgBGxzFo9xJS/zhM.QhQJDA64smO2BUYCXDKzSJNW6GK2Rre',
 'user', '普通用户', NOW());

INSERT IGNORE INTO `user` (id, username, password, role, nickname, create_time) VALUES
(3, 'zhangsan',
 '$2a$10$pNNUipgBGxzFo9xJS/zhM.QhQJDA64smO2BUYCXDKzSJNW6GK2Rre',
 'user', '张三', NOW());

-- ==================== 个人介绍 ====================
INSERT IGNORE INTO `profile` (id, title, content, create_time) VALUES
(1, '关于我', '欢迎来到我的博客！这里记录了我的技术学习和生活感悟。', NOW());

-- ==================== 博客文章 ====================
-- admin (id=1) 的文章
INSERT IGNORE INTO `blog` (id, title, content, summary, category, author_id, create_time) VALUES
(1, 'Spring Boot 入门指南',
 'Spring Boot 是 Spring 家族中的一个全新框架，它用来简化 Spring 应用程序的创建和开发过程。通过使用 Spring Boot，你可以轻松创建独立的、基于 Spring 的生产级应用程序。

## 核心特性

1. **自动配置** - Spring Boot 根据类路径中的依赖自动配置 Spring 应用
2. **起步依赖** - 将常用的依赖分组，简化构建配置
3. **嵌入式服务器** - 内嵌 Tomcat、Jetty 等，无需部署 WAR 包
4. **Actuator** - 提供生产级监控和管理功能

## 快速开始

创建一个 Spring Boot 项目很简单，只需要引入 spring-boot-starter-web 依赖，然后编写一个 @RestController 即可启动一个 Web 应用。

总的来说，Spring Boot 大大降低了 Spring 的入门门槛，是 Java 后端开发的利器。',
 'Spring Boot 是 Spring 家族中的全新框架，本文介绍其核心特性和快速入门方式。',
 'Java技术', 1, NOW());

INSERT IGNORE INTO `blog` (id, title, content, summary, category, author_id, create_time) VALUES
(2, 'MyBatis-Plus 使用技巧',
 'MyBatis-Plus（简称 MP）是 MyBatis 的增强工具，在 MyBatis 的基础上只做增强不做改变。

## 核心功能

### 1. CRUD 接口
继承 BaseMapper<T> 即可获得基础的增删改查方法，无需编写 XML 映射文件。

### 2. 条件构造器
LambdaQueryWrapper 提供了类型安全的条件构造方式，避免了硬编码字段名。

### 3. 分页插件
通过 PaginationInnerInterceptor 轻松实现分页功能。

### 4. 自动填充
MetaObjectHandler 可以自动填充 createTime、updateTime 等公共字段。

MyBatis-Plus 让数据库操作变得简单高效，是 Spring Boot 项目的首选 ORM 框架。',
 'MyBatis-Plus 是 MyBatis 的增强工具，本文介绍其 CRUD 接口、条件构造器和分页等核心功能。',
 'Java技术', 1, NOW());

-- user (id=2) 的文章
INSERT IGNORE INTO `blog` (id, title, content, summary, category, author_id, create_time) VALUES
(3, 'Vue 3 学习笔记 - Composition API',
 'Vue 3 引入了 Composition API，这是 Vue 框架的一次重大变革。

## 为什么需要 Composition API？

在 Vue 2 的 Options API 中，组件的逻辑分散在 data、methods、computed 等选项中。当组件变得复杂时，同一个功能的代码被分散在各个选项中，难以维护。

Composition API 允许我们将相关逻辑组织在一起，提升代码的可维护性和可复用性。

## 核心函数

- **ref / reactive** - 创建响应式数据
- **computed** - 计算属性
- **watch** - 监听数据变化
- **onMounted / onUnmounted** - 生命周期钩子

使用 Composition API 后，代码逻辑更加清晰，复用也更加方便。',
 'Vue 3 的 Composition API 是框架的重大变革，本文介绍其核心概念和使用方式。',
 '前端开发', 2, NOW());

INSERT IGNORE INTO `blog` (id, title, content, summary, category, author_id, create_time) VALUES
(4, 'Element Plus 组件库初体验',
 'Element Plus 是 Element UI 的 Vue 3 版本，是一套基于 Vue 3 的桌面端组件库。

## 常用组件

### 布局组件
el-container、el-aside、el-header、el-main 可以快速搭建后台管理布局。

### 表单组件
el-form、el-input、el-select 等覆盖了大部分表单场景。

### 表格组件
el-table 功能强大，支持排序、筛选和自定义模板。

### 反馈组件
el-message、el-message-box、el-dialog 提供友好的交互反馈。

Element Plus 文档详尽、社区活跃，是 Vue 3 后台项目的不二之选。',
 'Element Plus 是基于 Vue 3 的桌面端组件库，本文介绍其常用组件和使用感受。',
 '前端开发', 2, NOW());

-- zhangsan (id=3) 的文章
INSERT IGNORE INTO `blog` (id, title, content, summary, category, author_id, create_time) VALUES
(5, '从零搭建个人博客的心得',
 '最近花了一些时间，从零开始搭建了一个个人博客系统。过程中踩了不少坑，也学到了很多东西。

## 技术选型

后端我选择了 Spring Boot 3 + MyBatis-Plus + MySQL 的组合。Spring Boot 3 提供了强大的自动配置能力，MyBatis-Plus 让数据库操作变得异常简单。

前端采用了 Vue 3 + Vite + Element Plus。Vite 的构建速度非常快，Element Plus 的组件很完善，大大提升了开发效率。

## 遇到的坑

1. 跨域问题 - 通过 CORS 配置和 Vite 代理解决
2. JWT 认证 - 需要处理好 token 的刷新和过期
3. 前后端联调 - 统一接口返回格式很重要

总体来说，搭建个人博客是一次很有意义的技术实践。',
 '记录从零开始搭建个人博客的过程，包括技术选型、遇到的问题和解决方案。',
 '随笔', 3, NOW());

INSERT IGNORE INTO `blog` (id, title, content, summary, category, author_id, create_time) VALUES
(6, '程序员的日常',
 '很多人对程序员的工作充满好奇，今天就来聊聊程序员的日常。

## 上午 - 代码时间

早上 9 点到公司，先泡杯咖啡，然后打开 IDE 开始写代码。上午头脑最清醒，适合处理复杂的逻辑问题。

## 中午 - 充电

午餐时间是放松的时候，和同事们聊聊天，或者刷刷技术文章。

## 下午 - 会议与协作

下午通常会有各种会议：需求评审、代码评审、技术分享等。会议间隙继续写代码或者 review 同事的 PR。

## 晚上 - 持续学习

技术更新迭代很快，晚上回家后还会花时间学习新技术、做做 side project。

程序员这份工作虽然辛苦，但每一次解决 bug 的成就感、每一次新功能上线的满足感，都觉得一切值得。',
 '聊聊程序员的日常工作节奏：写代码、开会、学习，以及这份工作带来的成就感。',
 '生活', 3, NOW());

-- ==================== 留言 ====================
INSERT IGNORE INTO `message` (id, nickname, content, blog_id, create_time) VALUES
(1, '小明', '管理员你好！Spring Boot 那篇文章写得很清楚，对我入门帮助很大，期待更多 Java 相关的文章！', 1, NOW());
INSERT IGNORE INTO `message` (id, nickname, content, blog_id, create_time) VALUES
(2, '小红', '普通用户你好，Vue 3 的学习笔记写得很详细，Composition API 那段解释得非常清晰，收藏了！', 3, NOW());
INSERT IGNORE INTO `message` (id, nickname, content, blog_id, create_time) VALUES
(3, '匿名访客', '张三你好，你的博客搭建心得非常实用，我也正准备搭建自己的博客，这篇文章给了我很多启发。', 5, NOW());
INSERT IGNORE INTO `message` (id, nickname, content, blog_id, create_time) VALUES
(4, '技术小白', '请问 MyBatis-Plus 和 JPA 相比各有什么优劣？目前正在做技术选型，希望能得到一些建议。', 2, NOW());
INSERT IGNORE INTO `message` (id, nickname, content, blog_id, create_time) VALUES
(5, '前端新手', 'Element Plus 的表格组件确实好用，但是自定义模板部分有点复杂，希望博主能出一篇详细教程！', 4, NOW());
INSERT IGNORE INTO `message` (id, nickname, content, blog_id, create_time) VALUES
(6, '夜猫子程序员', '程序员的日常太真实了哈哈哈，晚上学习那段简直是我本人的写照。加油！', 6, NOW());