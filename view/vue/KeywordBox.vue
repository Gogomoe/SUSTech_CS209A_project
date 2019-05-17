<template>
    <div class="m-keyword"
         v-bind:class="{
         'v-fade-in': dataObj.fadeIn,
         'v-fade-out': dataObj.fadeOut,
         'v-hide': dataObj.hide,
         'shake shake-constant':dataObj.shake && !dataObj.fadeOut
         }"
         v-on:click="clickKeyword()">
        <div class="u-keyword">{{dataObj.keyword}}</div>
        <div class="u-detail" v-on:click="clickDetail()">
            <span v-if="!deleteMod">{{dataObj.detail}}</span>
            <antd-icon v-else type="delete-o" class="icon"/>
        </div>
    </div>

</template>

<script>
    export default {
        name: "KeywordBox",
        data() {
            return {}
        },
        props: ['dataObj', 'deleteMod'],
        methods: {
            clickKeyword: function () {
                if (!this.deleteMod) {
                    this.$emit('enter-category', {keyword: this.dataObj.keyword});
                }
            },
            clickDetail: function () {
                if (this.deleteMod) {
                    this.$emit('delete-keyword', {keyword: this.dataObj.keyword});
                }
            }
        }
    }
</script>

<style scoped>

</style>