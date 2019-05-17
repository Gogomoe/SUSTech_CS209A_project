package store

class FakeCategoryStore : CategoryStore {

    override fun save(save: CategoryStore.CategorySave?) {
    }

    override fun loadAllSaves(): List<CategoryStore.CategorySave> {
        return listOf(
                CategoryStore.CategorySave("手机", listOf(1L, 2L, 3L)),
                CategoryStore.CategorySave("电脑", listOf()),
                CategoryStore.CategorySave("Text1", listOf()),
                CategoryStore.CategorySave("Text2", listOf()),
                CategoryStore.CategorySave("Text3", listOf()),
                CategoryStore.CategorySave("Text4", listOf())
        )

    }

}