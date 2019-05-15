<template>
    <div id="g-body">

        <div id="g-main-container">
            <component
                    v-bind:is="currentPage"
                    v-bind:dataObj="dataObj"
                    v-on:enter-categorty="enterCategory($event.keyword)"
                    v-on:enter-product="enterProduct($event.productId)"
                    v-on:back-main-page="backToMainPage()"
                    v-on:back-category-page="backToCategoryPage()"
            ></component>
        </div>

    </div>
</template>

<script>
    import MainPage from './MainPage.vue';
    import CategoryPage from './CategoryPage.vue';
    import ProductPage from './ProductPage.vue';

    export default {
        name: "App",
        data() {
            return {
                currentPage: 'mainPage', //TODO
                dataObj: {
                    category: ''
                }
            }
        },
        computed: {},
        components: {
            'mainPage': MainPage,
            'categoryPage': CategoryPage,
            'productPage': ProductPage
        },
        methods: {
            enterCategory: function (keyword) {
                this.dataObj.category = keyword;
                this.currentPage = 'categoryPage';
            },
            enterProduct: function (productId) {
                this.dataObj.product = productId;
                this.currentPage = 'productPage';
            },
            backToCategoryPage: function () {
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