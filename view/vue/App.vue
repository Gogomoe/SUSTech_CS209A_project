<template>
    <div id="g-body">

        <div id="g-main-container">
            <component
                    v-bind:is="currentPage" v-on:enter-categorty="enterCategory($event.keyword)"
            ></component>
        </div>

    </div>
</template>

<script>
    import MainPage from './MainPage.vue';

    export default {
        name: "App",
        data() {
            return {
                currentPage: 'mainPage',
                topBar: 'topJumbotron'
            }
        },
        computed: {},
        components: {
            'mainPage': MainPage,
        },
        methods: {
            enterCategory: function (keyword) {

            }
        }
    }
</script>

<style lang="scss" scoped>
</style>