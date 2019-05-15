<template>
    <main id="m-category-page">

        <div class="m-history m-category-history"
             v-bind:class="{'show':isShowHistory}"></div>
        <div class="m-header">
            <div class="u-icon u-rollback" v-on:click="backToMainPage()">
                <antd-icon type="rollback-o" class="icon"/>
            </div>
            <div class="u-icon u-update"
                 v-bind:class="{'circle':isUpdating}"
                 v-on:click="update()">
                <antd-icon type="reload-o" class="icon"/>
            </div>
            <div class="u-icon u-arrow-up" v-on:click="showHistory()">
                <antd-icon type="arrow-up-o" class="icon"/>
            </div>
        </div>
        <div class="m-product-area g-page-container">

            <product-box
                    v-for="p of products"
                    v-bind:key="p.id"
                    v-bind:data-obj="p"
                    v-on:enter-product="enterProduct($event.productId)"
            ></product-box>

        </div>
    </main>
</template>

<script>
    import ProductBox from './ProductBox.vue';

    export default {
        name: "CategoryPage",
        components: {ProductBox},
        mounted() {
            this.loadProducts()
        },
        data() {
            return {
                products: [],
                isShowHistory: false,
                isUpdating: false
            }
        },
        props: ['dataObj'],
        methods: {
            getProducts: function () {
                let category = Category.getCategory(this.dataObj.category);
                let list = category.getProducts();
                let array = [];
                for (let i = 0; i < list.size(); i++) {
                    array.push({
                        name: list.get(i).getName(),
                        id: list.get(i).getId(),
                        url: list.get(i).getUrl(),
                        score: list.get(i).getComments().getScore(),
                    });
                }
                return array;
            },
            loadProducts: function () {
                return new Promise((resolve, reject) => {
                    let array = this.getProducts();
                    array.sort((a, b) => -(a.score - b.score));
                    array.forEach(it => {
                        it.fadeIn = false;
                        it.fadeOut = false;
                    });

                    for (let i = 0; i < array.length; i++) {
                        setTimeout(() => {
                            array[i].fadeIn = true;
                            this.products.push(array[i]);
                        }, i * 100);
                    }
                    setTimeout(resolve, array.length * 100 + 300);
                }).then(() => {
                    this.products.forEach(it => {
                        it.fadeIn = false;
                    });
                });
            },
            backToMainPage: function () {
                return new Promise((resolve, reject) => {
                    this.products.forEach(it => it.fadeOut = true);
                    setTimeout(resolve, 290);
                }).then(() => {
                    this.products = [];
                    this.$emit('back-main-page');
                });
            },
            showHistory: function () {
                if (!this.isShowHistory) {
                    //TODO animation
                }
                this.isShowHistory = !this.isShowHistory
            },
            enterProduct: function (id) {
                return new Promise((resolve, reject) => {
                    this.products.forEach(it => it.fadeOut = true);
                    setTimeout(resolve, 290);
                }).then(() => {
                    this.products = [];
                    this.$emit('enter-product', {productId: id});
                });
            }
        }
    }
</script>

<style scoped>

</style>