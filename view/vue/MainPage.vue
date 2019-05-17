<template>
    <main id="m-main-page" v-bind:class="{ 'v-delete': deleteMod }">
        <div class="m-header">
            <div class="u-icon u-delete" v-on:click="changeDeleteMod()">
                <antd-icon type="delete-o" class="icon"/>
            </div>
        </div>
        <div class="m-keyword-area g-page-container">
            <keyword-box
                    v-for="k of keywords"
                    v-bind:key="k.keyword"
                    v-bind:data-obj="k"
                    v-bind:delete-mod="deleteMod"
                    v-on:delete-keyword="deleteKeyword($event.keyword)"
                    v-on:enter-category="enterCategory($event.keyword)"
            >
            </keyword-box>

        </div>
    </main>
</template>

<script>
    import KeywordBox from './KeywordBox.vue';

    export default {
        name: "MainPage",
        components: {KeywordBox},
        data() {
            return {
                useless: 0,
                deleteMod: false,
                keywords: [],
            }
        },
        mounted() {
            this.loadKeywords()
        },
        methods: {
            getKeywords: function () {
                let list = Controller.getCategories();
                let array = [];
                for (let i = 0; i < list.size(); i++) {
                    array.push({
                        'keyword': list.get(i).getName(),
                        'detail': list.get(i).getProducts().size() + "条商品信息"
                    })
                }
                return array;
            },
            loadKeywords: function () {
                return new Promise((resolve, reject) => {
                    let ks = this.getKeywords();
                    ks.forEach(it => {
                        it.hide = true;
                        it.fadeOut = false;
                        it.fadeIn = false;
                        it.shake = false;
                    });
                    this.keywords.push(...ks);

                    for (let i = 0; i < ks.length; i++) {
                        setTimeout(() => {
                            ks[i].hide = false;
                            ks[i].fadeIn = true;
                        }, i * 100);
                    }
                    setTimeout(resolve, ks.length * 100 + 300);
                }).then(() => {
                    this.keywords.forEach(it => {
                        it.fadeIn = false;
                    });
                });
            },
            changeDeleteMod: function () {
                this.deleteMod = !this.deleteMod;
                for (let i = 0; i < this.keywords.length; i++) {
                    if (this.deleteMod) {
                        setTimeout(() => {
                            this.keywords[i].shake = true;
                        }, 100 / this.keywords.length * i);
                    } else {
                        this.keywords[i].shake = false;
                    }
                }
            },
            deleteKeyword: function (keyword) {
                return new Promise((resolve, reject) => {
                    let k = this.keywords.find(it => it.keyword === keyword);
                    k.fadeOut = true;
                    this.useless++;
                    setTimeout(resolve, 300);
                }).then(() => {
                    this.keywords = this.keywords.filter(it => it.keyword !== keyword);
                });
            },
            enterCategory: function (keyword) {
                return new Promise((resolve, reject) => {
                    this.keywords.forEach(it => it.fadeOut = true);
                    setTimeout(resolve, 300);
                }).then(() => {
                    this.keywords = [];
                    this.$emit('enter-categorty', {keyword: keyword})
                });
            }
        }
    }
</script>

<style scoped>

</style>