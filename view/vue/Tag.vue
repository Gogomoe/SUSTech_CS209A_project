<template>
    <div class="u-tag">
        <div class="u-name">{{dataObj.name}}</div>
        <input class="u-weight" type="text" v-model="num">
    </div>
</template>

<script>
    export default {
        name: "Tag",
        data() {
            return {
                oldNum: this.dataObj.weight
            }
        },
        props: ['dataObj'],
        computed: {
            num: {
                get: function () {
                    return this.oldNum;
                },
                set: function (newValue) {
                    let oldNum = this.oldNum;
                    this.oldNum = newValue.replace(/\D/g, '');
                    if (this.oldNum !== oldNum && this.oldNum) {
                        this.$emit('modify-weight', {name: this.dataObj.name, weight: this.oldNum});
                    }
                }
            }
        }
    }
</script>

<style scoped>

</style>