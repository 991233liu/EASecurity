<template>
  <div class="changePd-container">
    <el-dialog :visible.sync="dialogTableVisible" @close="closeDialog">
      <el-form ref="form" :model="form" :rules="rules" class="changePd-form" autocomplete="on" label-position="left">
        <div class="title-container">
          <h3 class="title">
            修改密码
          </h3>
        </div>

        <el-form-item prop="oldPassword">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="oldPasswordType"
            ref="oldPassword"
            v-model="form.oldPassword"
            :type="oldPasswordType"
            placeholder="旧密码"
            name="oldPassword"
            tabindex="2"
            autocomplete="on"
          />
          <span class="show-pwd" @click="showOldPwd">
            <svg-icon :icon-class="oldPasswordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>

        <el-form-item prop="newPassword">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="newPasswordType"
            ref="newPassword"
            v-model="form.newPassword"
            :type="newPasswordType"
            placeholder="新密码"
            name="newPassword"
            tabindex="2"
            autocomplete="on"
            @keyup.native="checkPassword"
          />
          <span class="show-pwd" @click="showNewPwd">
            <svg-icon :icon-class="newPasswordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>

        <el-form-item prop="captcha">
          <el-input
            ref="captcha"
            v-model="form.captcha"
            placeholder="验证码"
            name="captcha"
            type="text"
            tabindex="1"
            autocomplete="on"
          />
          <img :src="imgcode" class="picture" @click="captchas">
        </el-form-item>

        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="changePassword">
          修改密码
        </el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui

export default {
  name: 'ChangePassword',
  directives: { elDragDialog },
  data() {
    const validateOldPassword = (rule, value, callback) => {
      if (!value || value.length < 8) {
        callback(new Error('The oldPassword can not be less than 8 digits'))
      } else {
        callback()
      }
    }
    const validateNewPassword = (rule, value, callback) => {
      if (!this.checkPassword()) {
        callback(new Error('密码中只能包含字母、数字、特殊字符，至少8个字符，最多30个字符'))
      } else {
        callback()
      }
    }
    const validateCaptcha = (rule, value, callback) => {
      if (!value || value.length < 5) {
        callback(new Error('Please enter the correct captcha'))
      } else {
        callback()
      }
    }
    return {
      dialogTableVisible: false,
      form: {},
      rules: {
        oldPassword: [{ required: true, trigger: 'blur', validator: validateOldPassword }],
        newPassword: [{ required: true, trigger: 'blur', validator: validateNewPassword }],
        captcha: [{ required: true, trigger: 'blur', validator: validateCaptcha }]
      },
      oldPasswordType: 'password',
      newPasswordType: 'password',
      loading: false,
      imgcode: ''
    }
  },
  created() {
    // window.addEventListener('storage', this.afterQRScan)
    this.captchas()
  },
  mounted() {
    this.dialogTableVisible = true
  },
  methods: {
    // v-el-drag-dialog onDrag callback function
    closeDialog() {
      //      this.$refs.select.blur()
      //	  alert(this.$refs.select.blur())
      // 调用全局挂载的方法，关闭当前页
      this.$store.dispatch('tagsView/delView', this.$route)
      // 返回上一步路由
      this.$router.go(-1)
    },
    checkPassword() {
      var pwdRegex = new RegExp('(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,30}')
      if (!pwdRegex.test(this.form.newPassword)) {
        return false
      } else {
        return true
      }
    },
    showOldPwd() {
      if (this.oldPasswordType === 'password') {
        this.oldPasswordType = ''
      } else {
        this.oldPasswordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.oldPassword.focus()
      })
    },
    showNewPwd() {
      if (this.newPasswordType === 'password') {
        this.newPasswordType = ''
      } else {
        this.newPasswordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.newPassword.focus()
      })
    },
    captchas() {
      this.loading = true
      this.$store.dispatch('user/captchas').then((response) => {
        this.imgcode = response.image
        this.loading = false
      }).catch((error) => {
        console.log(error)
        this.loading = false
      })
    },
    changePassword() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/changePassword', this.form).then((response) => {
            this.$notify({
              title: '成功',
              message: '修改成功',
              type: 'success',
              duration: 5 * 1000
            })
            this.dialogTableVisible = false
            this.loading = false
          }).catch((error) => {
            console.log(error)
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* reset element-ui css */
.changePd-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 70%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
//      color: #fff;
      height: 47px;
//      caret-color: #fff;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px #283443 inset !important;
        -webkit-text-fill-color: #fff !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
.changePd-container {
  min-height: 100%;
  width: 100%;
  background-color: #2d3a4b;
  overflow: hidden;

  .changePd-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: #889aa4;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
//      color: #eee;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: #889aa4;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}

.picture{
//  width : 150px;
  height: 40px;
  float: right;
  margin-top: 4px;
  margin-right: 4px;
}

</style>
