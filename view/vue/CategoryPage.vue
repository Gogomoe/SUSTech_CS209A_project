<template>
    <main id="m-category-page">

        <div class="m-history m-category-history"
             v-bind:class="{'show':isShowHistory}">

            <div class="u-category-history-chart-box">
                <category-history-chart class="u-category-history-chart"
                                        v-if="isShowHistory"
                                        v-bind:category="dataObj.category"></category-history-chart>
            </div>

        </div>

        <div class="m-header">
            <div class="u-icon u-rollback" v-on:click="backToMainPage()">
                <antd-icon type="rollback-o" class="icon"/>
            </div>
            <div class="u-icon u-update"
                 v-bind:class="{'circle':isUpdating}"
                 v-on:click="updateProducts()">
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

                <div class="m-filter-area">
                    <input class="u-text-filter" type="text" placeholder="关键字过滤" v-model="filterText">
                </div>

                <div class="m-filter-area">
                    <div class="u-filter"
                         v-bind:class="{'filter-select':filterLengthStart === 0 && filterLengthEnd === 30}"
                         v-on:click="filterLength(0, 30)">
                        短评论
                    </div>
                    <div class="u-filter"
                         v-bind:class="{'filter-select':filterLengthStart === 30 && filterLengthEnd === 10000}"
                         v-on:click="filterLength(30, 10000)">
                        长评论
                    </div>
                </div>

                <div class="m-filter-area">
                    <div class="u-filter"
                         v-bind:class="{'filter-select':filterDateSelect === 3}"
                         v-on:click="filterDate(3)">
                        最近三天
                    </div>
                    <div class="u-filter"
                         v-bind:class="{'filter-select':filterDateSelect === 7}"
                         v-on:click="filterDate(7)">
                        最近一周
                    </div>
                    <div class="u-filter"
                         v-bind:class="{'filter-select':filterDateSelect === 30}"
                         v-on:click="filterDate(30)">
                        最近一月
                    </div>
                </div>
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
                        v-bind:max-score="maxScore"
                        v-on:enter-product="enterProduct($event.productId)"
                ></product-box>
            </div>

        </div>
    </main>
</template>

<script>
    import ProductBox from './ProductBox.vue';
    import Tag from './Tag.vue';
    import CategoryHistoryChart from './CategoryHistoryChart.vue';

    export default {
        name: "CategoryPage",
        components: {ProductBox, Tag, CategoryHistoryChart},
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
                sidebarFadeOut: false,
                maxScore: 0,
                filterDateSelect: -1,
                filterLengthStart: -1,
                filterLengthEnd: -1,
                filterTextField: ""
            }
        },
        computed: {
            filterText: {
                get: function () {
                    return this.filterTextField;
                },
                set: function (newValue) {
                    if (newValue.match(/\w+/g)) {
                        return;
                    }
                    this.filterTextField = newValue;

                    setTimeout(() => {
                        this.filterKeyword()
                    }, 0);
                }
            }
        },
        props: ['dataObj'],
        methods: {
            getProducts: function () {
                let category = Controller.getCategory(this.dataObj.category);
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
                    let max = 0;
                    array.sort((a, b) => -(a.score - b.score));
                    array.forEach(it => {
                        it.fadeIn = false;
                        it.fadeOut = false;
                        max = Math.max(max, it.score);
                    });
                    this.maxScore = max;

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
                let tags = Controller.getTagWeightsByCategory(this.dataObj.category);
                let array = [];
                for (let i = 0; i < tags.size(); i++) {
                    array.push({
                        name: tags.get(i).getContent(),
                        weight: tags.get(i).getWeight(),
                    });
                }
                return array;
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
                Controller.updateTag(name, parseInt(weight));
                let max = 0;
                this.products.forEach(it => {
                    it.score = Controller.calculateScore(it.id);
                    max = Math.max(max, it.score);
                });
                this.maxScore = max;
                this.products.sort((a, b) => -(a.score - b.score));
            },
            filterKeyword: function () {
                Controller.setFilterText(this.filterTextField);
                let max = 0;
                this.products.forEach(it => {
                    it.score = Controller.calculateFilterTextScore(it.id);
                    max = Math.max(max, it.score);
                });
                this.maxScore = max;
                this.products.sort((a, b) => -(a.score - b.score));
            },
            filterDate: function (limit) {
                if (this.filterDateSelect === limit) {
                    this.filterDateSelect = -1;
                } else {
                    this.filterDateSelect = limit;
                }
                Controller.setFilterDate(this.filterDateSelect);
                let max = 0;
                this.products.forEach(it => {
                    it.score = Controller.calculateFilterDateScore(it.id);
                    max = Math.max(max, it.score);
                });
                this.maxScore = max;
                this.products.sort((a, b) => -(a.score - b.score));
            },
            filterLength: function (start, end) {
                if (this.filterLengthStart === start && this.filterLengthEnd === end) {
                    this.filterLengthStart = -1;
                    this.filterLengthEnd = -1;
                } else {
                    this.filterLengthStart = start;
                    this.filterLengthEnd = end;
                }
                Controller.setFilterLength(this.filterLengthStart, this.filterLengthEnd);
                let max = 0;
                this.products.forEach(it => {
                    it.score = Controller.calculateFilterLengthScore(it.id);
                    max = Math.max(max, it.score);
                });
                this.maxScore = max;
                this.products.sort((a, b) => -(a.score - b.score));
            },
            updateProducts: function () {
                return new Promise((resolve, reject) => {
                    this.isUpdating = true;
                    Controller.updateProducts(this.dataObj.category);
                    setInterval(() => {
                        if (!Controller.isUpdating()) {
                            resolve();
                        }
                    }, 100)
                }).then(() => {
                    return new Promise((resolve, reject) => {
                        this.isUpdating = false;
                        this.products.forEach(it => it.fadeOut = true);
                        setTimeout(resolve, 290);
                    })
                }).then(() => {
                    this.products = [];
                    this.tags = [];
                    this.loadProducts();
                    this.loadTags();
                });
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