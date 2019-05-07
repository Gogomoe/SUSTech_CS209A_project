import Vue from 'vue';
import App from '../vue/App.vue'

import {DeleteOutline} from '@ant-design/icons';
import AntdIcon from '@ant-design/icons-vue';

AntdIcon.add(DeleteOutline);
Vue.use(AntdIcon);

new Vue({
    el: '#app-mount',
    template: '<App/>',
    components: {App}
});
