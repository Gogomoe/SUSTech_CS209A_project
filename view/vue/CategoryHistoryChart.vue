<template>
    <ve-line :data="chartData" :settings="chartSettings" :extend="chartExtend"></ve-line>
</template>

<script>
    export default {
        name: "CategoryHistoryChart",
        props: ['category'],
        data() {
            this.chartSettings = {
                metrics: ['评论数量'],
                dimension: ['日期'],
            };
            this.chartExtend = {
                legend: {
                    textStyle: {
                        color: '#fff',
                        fontSize: 16
                    }
                },
                xAxis: {
                    axisLabel: {
                        color: '#fff',
                        fontSize: 16
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff',
                        }
                    }
                },
                yAxis: {
                    axisLabel: {
                        color: '#fff',
                        fontSize: 16
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#fff',
                        }
                    }
                }
            };
            let data = Controller.getCategoryHistory(this.category);
            let array = [];
            for (let i = 0; i < data.size(); i++) {
                array.push({
                    '日期': data.get(i).getFirst(),
                    '评论数量': data.get(i).getSecond()
                })
            }
            return {
                chartData: {
                    columns: ['日期', '评论数量'],
                    rows: array
                }
            }
        }
    }

</script>

<style scoped>

</style>