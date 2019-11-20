import axios from "axios";
import Qs from "qs";
import router from '../../router'
import store from '../../store'
import storage from '../modeljs/storage.js';
import { Dialog } from 'vant';//弹窗函数，可直接调用


let cancel,
  promiseArr = {};
let count=0;

const CancelToken = axios.CancelToken;
axios.defaults.timeout = 30000;
/*axios.defaults.baseURL = '';*/      //统一设置baseURL

/*
axios.defaults.headers.common['token'] ='eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZWNyZXQiOiI2NjY2NjYiLCJleHAiOjE1NzM1MzQ1NzYsInVzZXJJZCI6IjE3In0.-ZiWIcXeZAzakSc7e3J7pO_cAs5yb1-Uoo6onv-CV4g';
*/


// 请求发送之前的拦截器
axios.interceptors.request.use(config => {
     if(store.state.token){//已登录登陆
      config.headers.common['token'] =store.state.token
     }
     return config
 }, error => {
     return Promise.reject(error)
 });

 // 请求响应拦截器
 axios.interceptors.response.use(res => {
     if(res){
         if(res.data){
             const data = res.data;
             if(data.resultCode == 10001){
                 Dialog({ message: '网络异常，请查看您的网络状态！' });
                 router.go(-1);
             }
         }
     }
     return res
 }, error => {
     if (error && error.response) {
         console.log(error);
     } else {
         console.log("http发生错误～～");
     }
     return Promise.resolve(error.response);

    });
/**
 * axios请求  设置
 */

/* axios.interceptors.request.use(
  config => {
    if (promiseArr[config.url]) {
      promiseArr[config.url]("操作取消");
      promiseArr[config.url] = cancel;
    } else {
      promiseArr[config.url] = cancel;
    }
    if(store.state.token){
        config.headers.token = store.state.token
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
); */
/**
 * 响应异常 拦截
 */

/* axios.interceptors.response.use(
  response => {
      // 登录状态过期统一拦截
      if(response){
          if(response.data){
              const data = response.data
              if(data.code === 3){
                  Message({
                      message:'登录状态过期',
                      type:'warning'
                  })
                  store.commit('setLoginState',false)
                  Cookie.remove('account')
                  Cookie.remove('uid')
                  Cookie.remove('token')
                  router.push('/login')
              }
          }
      }
    return response;
  },
  error => {
    if (error && error.response) {
      console.log(error);
    } else {
      console.log("http发生错误～～");
    }
    return Promise.resolve(error.response);
  }
);
 */
export default {
  get(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: "get",
        url: url,
        params: param,
        cancelToken: new CancelToken(c => {
          cancel = c;
        })
      })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  },
  post(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: "post",
        data:param, /*Qs.stringify(param),*/
        url:  url,
        cancelToken: new CancelToken(c => {
          cancel = c;
        })
      })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  },
    post1(url, param) {
        return new Promise((resolve, reject) => {
            axios({
                method: "post",
                data:Qs.stringify(param),
                url:  url,
                cancelToken: new CancelToken(c => {
                    cancel = c;
                })
            })
                .then(res => {
                    resolve(res);
                })
                .catch(err => {
                    reject(err);
                });
        });
    }
};
