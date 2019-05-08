package store

import model.*
import java.time.LocalDateTime

class FakeProductStore : ProductStore {

    val products = listOf(
            Product("红米 Redmi Note7Pro", 1L, "//img13.360buyimg.com/n7/jfs/t1/22679/31/6957/165164/5c650eacE9550017b/dede93ea26974929.jpg",
                    CommentSummary(
                            listOf(
                                    CommentQuery(
                                            listOf(
                                                    Comment(
                                                            1001L,
                                                            "买了 3 部，朋友家人各一部，都说很好，照相清晰，运行快，系统流畅，外观漂亮，配置高，性价比绝对高！",
                                                            LocalDateTime.of(2019, 5, 8, 20, 8, 0)
                                                    ),
                                                    Comment(
                                                            1001L,
                                                            "手机很喜欢，拍照清晰，声音大。早上下单当天送到，包装完好快递小哥服务好。",
                                                            LocalDateTime.of(2019, 5, 8, 20, 8, 10)
                                                    ),
                                                    Comment(
                                                            1001L,
                                                            "1600 买个相机，我有点奢侈",
                                                            LocalDateTime.of(2019, 5, 8, 20, 8, 28)
                                                    )
                                            ),
                                            listOf(
                                                    Tag("照相清晰", 1, 1.0),
                                                    Tag("运行快", 2, 1.0),
                                                    Tag("外观漂亮", 1, 1.0)
                                            ),
                                            LocalDateTime.of(2019, 5, 8, 20, 8, 40)
                                    )
                            ),
                            5.0
                    )
            ),
            Product("Apple iPhone XR", 2L, "//img10.360buyimg.com/n7/jfs/t1/3405/18/3537/69901/5b997c0aE5dc8ed9f/a2c208410ae84d1f.jpg",
                    CommentSummary(
                            listOf(
                                    CommentQuery(
                                            listOf(
                                                    Comment(
                                                            1004L,
                                                            "特别的好用",
                                                            LocalDateTime.of(2019, 5, 8, 20, 16, 28)
                                                    ),
                                                    Comment(
                                                            1005L,
                                                            "用了一段时间，刚开始手机有点发烫，不过后面就没事了，电池比我之前 8p 耐用，背板好像质感没有 8p 好，前者比较沉而且细腻，XR 就有点塑料感，搞不清是不是一样的材质，各方面都不错，打游戏流畅，降价有点快，不过自用的，好用就行了，如果黄色不加价得话，我还想入手黄色的",
                                                            LocalDateTime.of(2019, 5, 8, 20, 15, 40)
                                                    ),
                                                    Comment(
                                                            1006L,
                                                            "很多东西都降价了",
                                                            LocalDateTime.of(2019, 5, 8, 20, 15, 20)
                                                    )
                                            ),
                                            listOf(
                                                    Tag("流畅", 1, 1.0),
                                                    Tag("电池耐用", 1, 1.0)
                                            ),
                                            LocalDateTime.of(2019, 5, 8, 20, 16, 40)
                                    )
                            ),
                            4.0
                    )
            ),
            Product("华为荣耀 8X", 3L, "//img14.360buyimg.com/n7/jfs/t1/21333/14/5246/180334/5c3ad7b6Ef7d727c0/c16e93d0bf77a31f.jpg",
                    CommentSummary(
                            listOf(
                                    CommentQuery(
                                            listOf(
                                                    Comment(
                                                            1007L,
                                                            "买给妈妈用的，电话卡需要那种最小号的，还没用",
                                                            LocalDateTime.of(2019, 5, 8, 20, 16, 30)
                                                    ),
                                                    Comment(
                                                            1008L,
                                                            "暂时用着挺好的，挺喜欢",
                                                            LocalDateTime.of(2019, 5, 8, 20, 15, 29)
                                                    ),
                                                    Comment(
                                                            1009L,
                                                            "不错呀，价位合理，这个价位值了",
                                                            LocalDateTime.of(2019, 5, 8, 20, 15, 28)
                                                    )
                                            ),
                                            listOf(
                                                    Tag("价位合理", 1, 1.0)
                                            ),
                                            LocalDateTime.of(2019, 5, 8, 20, 16, 40)
                                    )
                            ),
                            4.5
                    )
            )
    )

    override fun save(product: Product?) {

    }

    override fun load(productId: Long): Product {
        return products[(productId - 1).toInt()]
    }


}