<template>
    <div id="g-body">

        <div id="g-main-container">
            <component
                    v-bind:is="currentPage"
                    v-bind:dataObj="dataObj"
                    v-on:enter-categorty="enterCategory($event.keyword)"
                    v-on:back-main-page="backToMainPage()"
            ></component>
        </div>

    </div>
</template>

<script>
    import MainPage from './MainPage.vue';
    import CategoryPage from './CategoryPage.vue';

    export default {
        name: "App",
        data() {
            return {
                currentPage: 'mainPage',
                dataObj: {
                    category: ''
                }
            }
        },
        computed: {},
        components: {
            'mainPage': MainPage,
            'categoryPage': CategoryPage,
        },
        methods: {
            enterCategory: function (keyword) {
                this.dataObj.category = keyword;
                this.currentPage = 'categoryPage';
            },
            backToMainPage: function () {
                this.currentPage = 'mainPage';
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>