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
        <div class="g-page-container">

            <div class="m-side-bar"
                 v-bind:class="{
                 'v-fade-in':sidebarFadeIn,
                 'v-fade-out':sidebarFadeOut
                 }">
                <div class="m-tag-area">

                    <tag
                            v-for="t of tags"
                            v-bind:key="t.name"
                            v-bind:data-obj="t"
                            v-on:modify-weight="modifyWeight($event.name,$event.weight)"
                    ></tag>

                </div>
            </div>
            <div class="m-product-area">
                <product-box
                        v-for="p of products"
                        v-bind:key="p.id"
                        v-bind:data-obj="p"
                        v-on:enter-product="enterProduct($event.productId)"
                ></product-box>
            </div>

        </div>
    </main>
</template>

<script>
    import ProductBox from './ProductBox.vue';
    import Tag from './Tag.vue';

    export default {
        name: "CategoryPage",
        components: {ProductBox, Tag},
        mounted() {
            this.loadProducts();
            this.loadTags();
        },
        data() {
            return {
                products: [],
                tags: [],
                isShowHistory: false,
                isUpdating: false,
                sidebarFadeIn: false,
                sidebarFadeOut: false
            }
        },
        props: ['dataObj'],
        methods: {
            getProducts: function () {
                // let category = Category.getCategory(this.dataObj.category);
                // let list = category.getProducts();
                // let array = [];
                // for (let i = 0; i < list.size(); i++) {
                //     array.push({
                //         name: list.get(i).getName(),
                //         id: list.get(i).getId(),
                //         url: list.get(i).getUrl(),
                //         score: list.get(i).getComments().getScore(),
                //     });
                // }
                // return array;

                let array = [
                    {
                        name: '红米 Redmi Note7Pro',
                        id: 1,
                        url: '//img13.360buyimg.com/n7/jfs/t1/22679/31/6957/165164/5c650eacE9550017b/dede93ea26974929.jpg',
                        score: 5.0
                    },
                    {
                        name: 'Apple iPhone XR',
                        id: 2,
                        url: '//img10.360buyimg.com/n7/jfs/t1/3405/18/3537/69901/5b997c0aE5dc8ed9f/a2c208410ae84d1f.jpg',
                        score: 4.0
                    },
                    {
                        name: '华为荣耀 8X',
                        id: 3,
                        url: '//img14.360buyimg.com/n7/jfs/t1/21333/14/5246/180334/5c3ad7b6Ef7d727c0/c16e93d0bf77a31f.jpg',
                        score: 4.5
                    }
                ];
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
            getTags: function () {
                return [
                    {name: '好看', weight: 3},
                    {name: '流畅', weight: 2},
                    {name: '性价比高', weight: 1}
                ]
            },
            loadTags: function () {
                return new Promise((resolve, reject) => {
                    let array = this.getTags();
                    this.tags.push(...array);
                    this.sidebarFadeIn = true;
                    setTimeout(resolve, 290);
                }).then(() => {
                    this.sidebarFadeIn = false;
                });
            },
            backToMainPage: function () {
                return new Promise((resolve, reject) => {
                    this.products.forEach(it => it.fadeOut = true);
                    this.sidebarFadeOut = true;
                    setTimeout(resolve, 290);
                }).then(() => {
                    this.products = [];
                    this.$emit('back-main-page');
                });
            },
            modifyWeight: function (name, weight) {
                //TODO
            },
            showHistory: function () {
                if (!this.isShowHistory) {
                    //TODO animation
                }
                this.isShowHistory = !this.isShowHistory
            },
            enterProduct: function (id) {
            }
        }
    }
</script>

<style scoped>

</style>