import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import { List,Button,Area, Uploader, Checkbox, CheckboxGroup,Row, Col, Icon,Tab, Tabs, DatetimePicker,Popup,NavBar,Field, Search,Cell,CellGroup} from 'vant'
import './style/common/base.styl'
/* import './plugins/element.js' */
Vue.config.productionTip = false;

// 全局注册
Vue.use(Cell).use(CellGroup).use(List).use(Icon).use(Tab).use(Tabs).use(Row).use(Col).use(Uploader).use(Button).use(Area).use(Checkbox).use(CheckboxGroup).use( DatetimePicker).use(Popup).use(NavBar).use(Field).use(Search);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
