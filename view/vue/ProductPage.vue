<template>
    <main id="m-product-page">

        <div class="m-history m-product-history"
             v-bind:class="{'show':isShowHistory}"></div>
        <div class="m-header">
            <div class="u-icon u-rollback" v-on:click="backToCategoryPage()">
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

        <div class="m-product-detail">

        </div>

    </main>
</template>

<script>
    export default {
        name: "ProductPage",
        mounted() {
            this.loadDetail()
        },
        data() {
            return {
                isShowHistory: false,
                isUpdating: false
            }
        },
        props: ['dataObj'],
        methods: {
            getDetail: function () {

            },
            loadDetail: function () {
                return new Promise((resolve, reject) => {

                });
            },
            backToCategoryPage: function () {

            },
            showHistory: function () {
                if (!this.isShowHistory) {
                    //TODO animation
                }
                this.isShowHistory = !this.isShowHistory
            }

        }
    }
</script>

<style scoped>

</style>