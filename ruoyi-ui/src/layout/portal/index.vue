<template>
  <div class="portal-container">
    <!-- 顶部导航栏 -->
    <div class="portal-header">
      <div class="header-content">
        <div class="logo" @click="goHome">
          <img src="@/assets/logo/logo.png" alt="logo" class="logo-img" />
          <span class="logo-title">考研互助交流平台</span>
        </div>
        <div class="nav-menu">
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            @select="handleSelect"
            background-color="#fff"
            text-color="#333"
            active-text-color="#409EFF"
          >
            <el-menu-item index="/portal/home">
              <i class="el-icon-s-home"></i>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/portal/university">
              <i class="el-icon-school"></i>
              <span>院校信息</span>
            </el-menu-item>
            <el-menu-item index="/portal/material">
              <i class="el-icon-folder-opened"></i>
              <span>资料中心</span>
            </el-menu-item>
            <el-menu-item index="/portal/forum">
              <i class="el-icon-chat-dot-round"></i>
              <span>考研论坛</span>
            </el-menu-item>
          </el-menu>
        </div>
        <div class="user-info">
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-dropdown">
              <el-avatar :size="32" :src="avatar">
                <img src="@/assets/images/profile.jpg" />
              </el-avatar>
              <span class="username">{{ name }}</span>
              <i class="el-icon-arrow-down"></i>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">
                <i class="el-icon-user"></i> 个人中心
              </el-dropdown-item>
              <el-dropdown-item command="myPost">
                <i class="el-icon-document"></i> 我的帖子
              </el-dropdown-item>
              <el-dropdown-item command="myCollect">
                <i class="el-icon-star-off"></i> 我的收藏
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <i class="el-icon-switch-button"></i> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!-- 主体内容 -->
    <div class="portal-main">
      <router-view />
    </div>
    <!-- 底部 -->
    <div class="portal-footer">
      <p>考研互助交流平台 © 2024 All Rights Reserved</p>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'PortalLayout',
  computed: {
    ...mapGetters(['avatar', 'name']),
    activeMenu() {
      const route = this.$route
      const { path } = route
      // 处理详情页的高亮
      if (path.startsWith('/portal/university')) return '/portal/university'
      if (path.startsWith('/portal/material')) return '/portal/material'
      if (path.startsWith('/portal/forum') || path.startsWith('/portal/post')) return '/portal/forum'
      return path
    }
  },
  methods: {
    goHome() {
      this.$router.push('/portal/home')
    },
    handleSelect(index) {
      this.$router.push(index)
    },
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/portal/profile')
          break
        case 'myPost':
          this.$router.push('/portal/profile/post')
          break
        case 'myCollect':
          this.$router.push('/portal/profile/collect')
          break
        case 'logout':
          this.$store.dispatch('LogOut').then(() => {
            location.href = '/login'
          })
          break
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.portal-header {
  height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #e8e8e8;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;

  .header-content {
    max-width: 1400px;
    margin: 0 auto;
    height: 100%;
    display: flex;
    align-items: center;
    padding: 0 20px;
  }

  .logo {
    display: flex;
    align-items: center;
    cursor: pointer;
    margin-right: 40px;

    .logo-img {
      height: 36px;
      width: 36px;
    }

    .logo-title {
      font-size: 18px;
      font-weight: 600;
      color: #333;
      margin-left: 10px;
      white-space: nowrap;
    }
  }

  .nav-menu {
    flex: 1;

    .el-menu {
      border-bottom: none;

      .el-menu-item {
        font-size: 15px;
        height: 60px;
        line-height: 60px;

        i {
          margin-right: 5px;
        }
      }
    }
  }

  .user-info {
    .user-dropdown {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 5px 10px;

      &:hover {
        background-color: #f5f5f5;
      }

      .username {
        margin: 0 8px;
        font-size: 14px;
        color: #333;
      }
    }
  }
}

.portal-main {
  flex: 1;
  margin-top: 60px;
  min-height: calc(100vh - 60px - 60px);
}

.portal-footer {
  height: 60px;
  background-color: #333;
  display: flex;
  align-items: center;
  justify-content: center;

  p {
    color: #999;
    font-size: 14px;
    margin: 0;
  }
}
</style>
