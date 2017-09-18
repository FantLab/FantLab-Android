package org.odddev.fantlab.core

/**
 * todo 1. AuthorController: убрать костыли с отображением лоадера
 * todo 2. параллельная загрузка отзывов и изданий с добавлением в AuthorFull и обновлением AuthorController
 * todo 3. Epoxy controllers: придумать что-нибудь с id
 * todo 4. AuthorController: поправить количество отзывов ("нет отзывов" вместо "0 отзывов")
 * todo 5. AuthorController: убрать AutoModel
 * todo 6. Epoxy controllers: решить вопрос с import alias (сейчас генератор не резолвит)
 * todo 7. после добавления базы передавать id автора вместо биографии строкой
 * todo 8. организовать кеш шрифтов (BindingUtils -> bindFont)
 * todo 9. загрузка изображений в TextView (биография etc)
 * todo 10. FormatUtils: поправить обработку вложенных тегов
 * todo 11. FormatUtils: нормальная обработка тегов `[q`],`[h`], обработка `[LIST`], `[VIDEO`] (see https://stackoverflow.com/a/42370792)
 * todo 12. FormatUtils: smiles :)
 * todo 13. распарсить нормально Author, Work, Nomination (и убрать AutorFull)
 * todo 14. для порядка works в author завести модель AuthorWorks = { work_id, position, deep, type }
 * todo 15. для порядка authors в work завести модель WorkAuthors = { author_id, author_name, type }
 * */
