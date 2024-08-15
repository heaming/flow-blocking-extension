import { createApp } from 'vue'
import {createBootstrap} from "bootstrap-vue-next";
import './style.css'
import App from './App.vue'
import VueCookies from 'vue3-cookies'

// Add the necessary CSS
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'

createApp(App).use(VueCookies, {
    expireTimes: "365d"
}).use(createBootstrap()).mount('#app')

