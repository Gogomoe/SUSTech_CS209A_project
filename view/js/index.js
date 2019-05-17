import Vue from 'vue';
import App from '../vue/App.vue'

import {DeleteOutline, RollbackOutline, ArrowUpOutline, ReloadOutline} from '@ant-design/icons';
import AntdIcon from '@ant-design/icons-vue';

AntdIcon.add(DeleteOutline);
AntdIcon.add(RollbackOutline);
AntdIcon.add(ArrowUpOutline);
AntdIcon.add(ReloadOutline);
Vue.use(AntdIcon);

new Vue({
    el: '#app-mount',
    template: '<App/>',
    components: {App}
});
