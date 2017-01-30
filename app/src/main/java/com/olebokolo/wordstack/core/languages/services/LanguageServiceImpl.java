package com.olebokolo.wordstack.core.languages.services;

import com.olebokolo.wordstack.core.languages.dao.LanguageDao;
import com.olebokolo.wordstack.core.languages.init.LanguagesInitService;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.utils.Comparator;

import org.apache.commons.lang3.LocaleUtils;

import java.util.List;
import java.util.Locale;

import lombok.Setter;

@Setter
public class LanguageServiceImpl implements LanguageService{

    private LanguageDao dao;
    private LanguagesInitService initService;
    private Comparator comparator;

    @Override
    public boolean areLanguagesInDatabase() {
        List<Language> languageList = dao.getAll();
        return comparator.saysNotEmpty(languageList);
    }

    @Override
    public boolean noLanguagesInDatabase() {
        return !areLanguagesInDatabase();
    }

    @Override
    public List<Language> getAllLanguages() {
        initService.initLanguagesInDatabaseIfNeeded();
        return dao.getAll();
    }

    @Override
    public Language findByShortName(String shortName) {
        return dao.findByShortName(shortName);
    }

    @Override
    public Language findById(Long id) {
        return Language.findById(Language.class, id);
    }

    @Override
    public Locale getLocaleForLanguage(Language language) {
        switch (language.getShortName()) {
            case "ar": return LocaleUtils.toLocale("ar");
            case "az": return LocaleUtils.toLocale("az_AZ");
            case "be": return LocaleUtils.toLocale("be_BY");
            case "bg": return LocaleUtils.toLocale("bg_BG");
            case "bs": return LocaleUtils.toLocale("bs_BA");
            case "cs": return LocaleUtils.toLocale("cs_CZ");
            case "cy": return LocaleUtils.toLocale("cy_GB");
            case "da": return LocaleUtils.toLocale("da_DK");
            case "de": return LocaleUtils.toLocale("de_DE");
            case "el": return LocaleUtils.toLocale("el_GR");
            case "en": return LocaleUtils.toLocale("en_US");
            case "es": return LocaleUtils.toLocale("es_ES");
            case "et": return LocaleUtils.toLocale("et_EE");
            case "fa": return LocaleUtils.toLocale("fa_IR");
            case "fi": return LocaleUtils.toLocale("fi_FI");
            case "fr": return LocaleUtils.toLocale("fr_FR");
            case "ga": return LocaleUtils.toLocale("ga_IE");
            case "he": return LocaleUtils.toLocale("he_IL");
            case "hr": return LocaleUtils.toLocale("hr_HR");
            case "hu": return LocaleUtils.toLocale("hu_HU");
            case "hy": return LocaleUtils.toLocale("hy_AM");
            case "is": return LocaleUtils.toLocale("is_IS");
            case "it": return LocaleUtils.toLocale("it_IT");
            case "ja": return LocaleUtils.toLocale("ja_JP");
            case "ka": return LocaleUtils.toLocale("ka_GE");
            case "ko": return LocaleUtils.toLocale("ko_KR");
            case "ky": return LocaleUtils.toLocale("ky_KG");
            case "lt": return LocaleUtils.toLocale("lt_LT");
            case "lv": return LocaleUtils.toLocale("lv_LV");
            case "mk": return LocaleUtils.toLocale("mk_MK");
            case "mn": return LocaleUtils.toLocale("mn_MN");
            case "nl": return LocaleUtils.toLocale("nl_NL");
            case "no": return LocaleUtils.toLocale("nb_NO");
            case "pl": return LocaleUtils.toLocale("pl_PL");
            case "pt": return LocaleUtils.toLocale("pt_PT");
            case "ro": return LocaleUtils.toLocale("ro_RO");
            case "ru": return LocaleUtils.toLocale("ru_RU");
            case "sk": return LocaleUtils.toLocale("sk_SK");
            case "sl": return LocaleUtils.toLocale("sl_SL");
            case "sq": return LocaleUtils.toLocale("sq_AL");
            case "sr": return LocaleUtils.toLocale("sr_RS");
            case "sv": return LocaleUtils.toLocale("sv_SE");
            case "tg": return LocaleUtils.toLocale("tg");
            case "th": return LocaleUtils.toLocale("th_TH");
            case "tr": return LocaleUtils.toLocale("tr_TR");
            case "uk": return LocaleUtils.toLocale("uk_UA");
            case "uz": return LocaleUtils.toLocale("uz_UZ");
            case "vi": return LocaleUtils.toLocale("vi_VN");
            case "zh": return LocaleUtils.toLocale("zh_CN");
        }
        return null;
    }

}
