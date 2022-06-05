# EASecurity--SecurityCentre

## 访问路径  
- **打开登陆页**  
http://128.0.0.2/SecurityCentre/auth/login  
<br>
- **打开登陆测试页**  
http://128.0.0.2/SecurityCentre/auth/loginTest.html  
<br>

## 其它（临时）  

#### OAT（OpaqueAccessToken）  
数据结构： 

```java  
{
  "access_token": "7b2a9b9b9c3f480bb8641c2cf0dd74c6",
  "expires": "2022-06-04T08:29:13.935Z",
  "expires_in": 3600,
  "refresh_token": "47654f2df6b843c2b73f8ccd2d8fce43",
  "token_type": "Bearer"
}
```
access_token：访问令牌（访问凭证）。由客户端（浏览器、APP等）发送给后台应用的身份标识。应用后台可以使用access_token去认证中心兑换jwt信息。access_token一般有效期很短，并且与jwt的有权限一直。access_token目前只支持一个，所有系统共享一个，需要支持多个系统吗？？？  
expires：过期时间，超过这个时间则此access_token会失效。  
expires_in：有效期。非必须字段，单位秒。  
refresh_token：刷新令牌。当access_token过期后（或者其它需要刷新访问令牌的情况），可持此去认证中心兑换新的访问令牌。refresh_token有效期很长，但是只能使用一次。成功刷新访问令牌后，会返回新的refresh_token。  

#### JWT（Json Web Token）  
数据结构：标准的jwt数据结构，其中‘userDetails’字段为用户身份信息。    
jwt不对“外网”开放，它的有效期与access_token相同，且一一对应。应用后台可以将其缓存下来，这样访问性能更高。也可实时去认证中心获取。jwt的获取，可以使用access_token，也可以使用cookie。  

#### 客户端使用说明  
- 获取access_token：  
在登录页或者登录请求中增加“redirect_url=成功登录后的目标跳转地址”和“loginType=accessToken”。认证中心会在成功登录后跳转到“redirect_url”，同时在跳转的URL中增加“token=xxx”。目标地址可以从URL中获取完整的OAT信息。  


- 刷新access_token：  
请求“http://ip:port/SecurityCentre/token/refreshToken”。在Headers中增加“authorization = Bearer 7b2a9b9b9c3f480bb8641c2cf0dd74c6”（7b2a9b9b9c3f480bb8641c2cf0dd74c6 为当前的access_token）；在请求参数中增加“refresh_token=47654f2df6b843c2b73f8ccd2d8fce43”。  


- 使用access_token认证（以下3中方法，任选一个）：  
1、在Headers中增加“authorization: Bearer 7b2a9b9b9c3f480bb8641c2cf0dd74c6”（7b2a9b9b9c3f480bb8641c2cf0dd74c6 为当前的access_token）。  
2、在Headers中增加“access_token: 7b2a9b9b9c3f480bb8641c2cf0dd74c6”。  
3、在请求参数中增加“access_token = 7b2a9b9b9c3f480bb8641c2cf0dd74c6”。  

    