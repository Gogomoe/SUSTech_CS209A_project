<template>
    <div class="m-product-box"
         v-bind:class="{
         'v-fade-in':dataObj.fadeIn,
         'v-fade-out':dataObj.fadeOut
         }"
         v-on:click="$emit('enter-product', {productId: dataObj.id})">
        <img v-bind:src="dataObj.url" v-bind:alt="dataObj.name">
        <div class="title-box">
            <h1>
                {{dataObj.name}}
            </h1>
        </div>
        <div class="score-box"
             v-bind:style="{ 'font-size' :1.6 * dataObj.score / maxScore + 0.8 + 'em' }">
            {{dataObj.score | scoreFilter}}
        </div>
    </div>
</template>

<script>
    export default {
        name: "ProductBox",
        data() {
            return {}
        },
        props: ['dataObj', 'maxScore'],
        filters: {
            scoreFilter(value) {
                return parseFloat(value).toFixed(1);
            }
        },
        methods: {}
    }
</script>

<style scoped>

</style>