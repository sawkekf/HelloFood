package com.refrigerator.springboot.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class IngRepositoryCusImp implements IngRepositoryCus {
    @Autowired
    private TagRepository tagRepository;
    private JPAQueryFactory queryFactory;
    public IngRepositoryCusImp(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }
    @Override
    public IngredientDto getIng(Long sid) {
        QIngredient ingredient = QIngredient.ingredient;

        IngredientDto ingredientDto =
                queryFactory
                        .select(new QIngredientDto(ingredient.id,ingredient.ing_deadline,ingredient.smallTag.tag_small_name,ingredient.reg_time,ingredient.textareas))
                        .from(ingredient)
                        .where(ingredient.id.eq(sid)).fetchOne();
        return ingredientDto;


    }

    @Override
    public Long updateIng(IngredientDto ingredientDto) {
        QIngredient ingredient = QIngredient.ingredient;
        SmallTags smallTags = getsmallTag(ingredientDto.getSmallTags());
        long count = queryFactory
                .update(ingredient).set(ingredient.textareas,ingredientDto.getTexts())
                .set(ingredient.ing_deadline,ingredientDto.getIng_deadline())
                .set(ingredient.smallTag,smallTags).where(ingredient.id.eq(ingredientDto.getId())).execute();

        return  count;
    }

    @Override
    public SmallTags getsmallTag(String id) {
        System.out.println(id);

        SmallTags smallTags = tagRepository.findbyname(id);
        return smallTags;
    }

    //
    @Override
    public List<SmallDto> getLists(String ssname) {

       QSmallTags qsmallTags = QSmallTags.smallTags;

       List<SmallDto> lists = queryFactory
               .select( new QSmallDto(qsmallTags.id,qsmallTags.tag_small_name,qsmallTags.bigTags.id))
               .from(qsmallTags)
               .where(qsmallTags.tag_small_name.like("%"+ssname+"%"))
               .fetch();
        return  lists;
    }

    @Override
    public Long deleteIng(String str, Long ref) {
        return null;
    }


    public Long deleteIng(String str) {
        QIngredient ingredient = QIngredient.ingredient;
        Long count = queryFactory
                .delete(ingredient)
                .where(ingredient.id.eq(Long.parseLong(str)))
                .execute();

        return count;
    }

    @Override
    public Long deleteIngs(String ids, Long id) {
        return null;
    }


    public Long deleteIngs(String ids) {
        QIngredient ingredient = QIngredient.ingredient;
        Long count = queryFactory
                .delete(ingredient)
                .where(ingredient.id.eq(Long.parseLong(ids)))
                .execute();
        return count;
    }

    @Override
    public Long updateIng2(IngredientDto ingredientDto) {
        QIngredient ingredient = QIngredient.ingredient;
        SmallTags smallTags = getsmallTag(ingredientDto.getSmallTags());
        long count = queryFactory
                .update(ingredient)
                .set(ingredient.ing_deadline,ingredientDto.getIng_deadline())
                .set(ingredient.smallTag,smallTags).where(ingredient.id.eq(ingredientDto.getId())).execute();

        return  count;
    }

    public Long updateIng3(ListDetailDto listDetailDto) {
        QIngredient ingredient = QIngredient.ingredient;
        long count = queryFactory
                .update(ingredient)
                .set(ingredient.ing_deadline,listDetailDto.getDaeLine())
                .set(ingredient.textareas,listDetailDto.getMemo()).where(ingredient.id.eq(Long.parseLong(listDetailDto.getIngId()))).execute();

        return  count;
    }
    @Override
    public List<SmallTags> getSmList() {
        QSmallTags smallTags = QSmallTags.smallTags;
        List<SmallTags> lists = queryFactory
                .selectFrom(smallTags)
                .fetch();

        return  lists;
    }


    public List<BigTags> getbiList() {
        QBigTags bigTags = QBigTags.bigTags;
        List<BigTags> lists = queryFactory
                .selectFrom(bigTags)
                .fetch();

        return  lists;

    }

    @Override
    public List<SmallDto> selectByBig(String text) {
        QBigTags qBigTags = QBigTags.bigTags;
        QSmallTags qsmallTags = QSmallTags.smallTags;
        List<SmallDto> lists = queryFactory

        .select( new QSmallDto(qsmallTags.id,qsmallTags.tag_small_name,qsmallTags.bigTags.id))
                .from(qsmallTags)
                .where(qsmallTags.bigTags.big_name.eq(text))
                .fetch();
        return  lists;
    }

    public List<IngreBigDto> allList(Long id) {
        QIngredient ingredient = QIngredient.ingredient;
        QBigTags bigTags = QBigTags.bigTags;
        QtagBigImg qtagBigImg = QtagBigImg.tagBigImg;
        List<IngreBigDto> inlits =
                queryFactory
                .select(new QIngreBigDto(ingredient.id,ingredient.ing_deadline,ingredient.smallTag.tag_small_name,bigTags.id,qtagBigImg.srcs,bigTags,ingredient.textareas))
                .from(ingredient,bigTags,qtagBigImg)
                .where(ingredient.smallTag.bigTags.id.eq(bigTags.id).and(bigTags.id.eq(qtagBigImg.bigTags.id)).and(ingredient.refrigerator.id.eq(id)))
              .fetch();

        return inlits;
    }


    //3가지 entity를 참고하여 텍스트를 입력할 시 텍스트를 포함하고 있는 smallTag를 가진 ingredient의 데이터를 가져온다.
    public List<IngreBigDto> getInglist(String text, Long id) {
        QIngredient ingredient = QIngredient.ingredient;
        QBigTags bigTags = QBigTags.bigTags;

        QtagBigImg qtagBigImg = QtagBigImg.tagBigImg;
        List<IngreBigDto> inlits =
                queryFactory
                        .select(new QIngreBigDto(ingredient.id,ingredient.ing_deadline,ingredient.smallTag.tag_small_name,bigTags.id,qtagBigImg.srcs,bigTags,ingredient.textareas))
                        .from(ingredient,bigTags,qtagBigImg)
                        .where(ingredient.smallTag.bigTags.id.eq(bigTags.id).and(bigTags.id.eq(qtagBigImg.bigTags.id))
                                .and(ingredient.smallTag.tag_small_name.contains(text)).and(ingredient.refrigerator.id.eq(id)))
                        .fetch();
        return inlits;
    }

    public void insertIng(Selectdto selectdto, Long ref) {


    }

    public IngreBigDto getIng2(Long id,Long ref) {
        QIngredient ingredient = QIngredient.ingredient;
        QBigTags bigTags = QBigTags.bigTags;

        QtagBigImg qtagBigImg = QtagBigImg.tagBigImg;
        IngreBigDto inlits =
                queryFactory
                        .select(new QIngreBigDto(ingredient.id,ingredient.ing_deadline,ingredient.smallTag.tag_small_name,bigTags.id,qtagBigImg.srcs,bigTags,ingredient.textareas))
                        .from(ingredient,bigTags,qtagBigImg)
                        .where(ingredient.smallTag.bigTags.id.eq(bigTags.id).and(bigTags.id.eq(qtagBigImg.bigTags.id))
                                .and(ingredient.refrigerator.id.eq(ref)).and(ingredient.id.eq(id)))
                        .fetchOne();
        return inlits;
    }

    public IngreBigDto getIngs(Long s) {
        QIngredient ingredient = QIngredient.ingredient;
        QBigTags bigTags = QBigTags.bigTags;

        QtagBigImg qtagBigImg = QtagBigImg.tagBigImg;
        IngreBigDto inlits =
                queryFactory
                        .select(new QIngreBigDto(ingredient.id,ingredient.ing_deadline,ingredient.smallTag.tag_small_name,bigTags.id,qtagBigImg.srcs,bigTags,ingredient.textareas))
                        .from(ingredient,bigTags,qtagBigImg)
                        .where(ingredient.smallTag.bigTags.id.eq(bigTags.id).and(bigTags.id.eq(qtagBigImg.bigTags.id))
                                .and(ingredient.id.eq(s)))
                        .fetchOne();
        return inlits;

    }
    public Ingredient getIngs2(String s) {
        QIngredient ingredient = QIngredient.ingredient;
        QBigTags bigTags = QBigTags.bigTags;
        QtagImg tagImg = QtagImg.tagImg;
        QtagBigImg qtagBigImg = QtagBigImg.tagBigImg;
        Ingredient inlits =
                queryFactory
                        .select(ingredient)
                        .from(ingredient)
                        .where(ingredient.id.eq(Long.parseLong(s)))

                        .fetchOne();
        return inlits;

    }
    public Ingredient getIngs3(String s) {
        QIngredient ingredient = QIngredient.ingredient;
        QBigTags bigTags = QBigTags.bigTags;
        QtagImg tagImg = QtagImg.tagImg;
        QtagBigImg qtagBigImg = QtagBigImg.tagBigImg;
        Ingredient inlits =
                queryFactory
                        .select(ingredient)
                        .from(ingredient)
                        .where(ingredient.id.eq(Long.parseLong(s)))

                        .fetchOne();
        return inlits;

    }
}
