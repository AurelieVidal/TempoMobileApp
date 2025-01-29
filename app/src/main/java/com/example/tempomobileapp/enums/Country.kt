package com.example.tempomobileapp.enums

import com.example.tempomobileapp.R

/**
 * data class Country represents a country's phone information.
 * Used for component 'InputPhone', used in sign in page
 */
data class Country(
    val name: String,
    val code: String,
    val phonePrefix: String,
    val flagResId: Int,
    val phoneFormat: String
)

private const val DOMINICAN_REPUBLIC = "République dominicaine"

val countries = listOf(
    Country("Format libre", "", "", R.drawable.flag_white, ""),
    Country("Afghanistan", "AF", "+93", R.drawable.flag_afghanistan, "+93 ## ######"),
    Country("Afrique du Sud", "ZA", "+27", R.drawable.flag_south_africa, "+27 ## ### ####"),
    Country("Îles Åland", "AX", "+358", R.drawable.flag_aland_islands, "+358########"),
    Country("Albanie", "AL", "+355", R.drawable.flag_albania, "+355 # ### ####"),
    Country("Algérie", "DZ", "+213", R.drawable.flag_algeria, "+213 ## #######"),
    Country("Allemagne", "DE", "+49", R.drawable.flag_germany, "+49 ### ### ####"),
    Country("Andorre", "AD", "+376", R.drawable.flag_andorra, "+376 ## ## ##"),
    Country("Angola", "AO", "+244", R.drawable.flag_angola, "+244 #########"),
    Country("Anguilla", "AI", "+1 264", R.drawable.flag_anguilla, "+1 264 ### ####"),
    Country(
        "Antigua-et-Barbuda",
        "AG",
        "+1 268",
        R.drawable.flag_antigua_and_barbuda,
        "+1 268 ### ####"
    ),
    Country("Antarctique", "AQ", "+672", R.drawable.flag_antarctica, "+672 ## ####"),
    Country("Arabie saoudite", "SA", "+966", R.drawable.flag_saudi_arabia, "+966 ## #### ####"),
    Country("Argentine", "AR", "+54", R.drawable.flag_argentina, "+54 ### ### ####"),
    Country("Arménie", "AM", "+374", R.drawable.flag_armenia, "+374 ## ### ###"),
    Country("Aruba", "AW", "+297", R.drawable.flag_aruba, "+297 #######"),
    Country("Ascension", "SH", "+247", R.drawable.flag_ascension_island, "+247 ####"),
    Country("Australie", "AU", "+61", R.drawable.flag_australia, "+61 # #### ####"),
    Country("Autriche", "AT", "+43", R.drawable.flag_austria, "+43 ### ### ####"),
    Country("Azerbaïdjan", "AZ", "+994", R.drawable.flag_azerbaijan, "+994 ## ### ####"),
    Country("Bahamas", "BS", "+1242", R.drawable.flag_bahamas, "+1 242 ### ####"),
    Country("Bahreïn", "BH", "+973", R.drawable.flag_bahrain, "+973 #### ####"),
    Country("Bangladesh", "BD", "+880", R.drawable.flag_bangladesh, "+880 #### ### ###"),
    Country("Barbade", "BB", "+1246", R.drawable.flag_barbados, "1 246 ### ####"),
    Country("Belgique", "BE", "+32", R.drawable.flag_belgium, "+32 # ### ####"),
    Country("Belize", "BZ", "+501", R.drawable.flag_belize, "+501 #######"),
    Country("Bénin", "BJ", "+229", R.drawable.flag_benin, "229 ######"),
    Country("Bermudes", "BM", "+1 441", R.drawable.flag_bermuda, "+1 441 ### ####"),
    Country("Bhoutan", "BT", "+975", R.drawable.flag_bhutan, "+975 ######"),
    Country("Biélorussie", "BY", "+375", R.drawable.flag_belarus, "+375 ## #######"),
    Country("Birmanie", "MM", "+95", R.drawable.flag_myanmar, "+95 #########"),
    Country("Bolivie", "BO", "+591", R.drawable.flag_bolivia, "591 # ### ####"),
    Country(
        "Bosnie-Herzégovine",
        "BA",
        "+387",
        R.drawable.flag_bosnia_and_herzegovina,
        "+387 ## ######"
    ),
    Country("Botswana", "BW", "+267", R.drawable.flag_botswana, "+267 ## ### ###"),
    Country("Île Bouvet", "BV", "+47", R.drawable.flag_norway, "+47 #### ####"),
    Country("Brésil", "BR", "+55", R.drawable.flag_brazil, "+55 ## ##### ####"),
    Country("Brunei", "BN", "+673", R.drawable.flag_brunei, "+673 ### ####"),
    Country("Bulgarie", "BG", "+359", R.drawable.flag_bulgaria, "+359 # #######"),
    Country("Burkina Faso", "BF", "+226", R.drawable.flag_burkina_faso, "+226 ########"),
    Country("Burundi", "BI", "+257", R.drawable.flag_burundi, "+257 #########"),
    Country("Cambodge", "KH", "+855", R.drawable.flag_cambodia, "+855 ## ### ####"),
    Country("Cameroun", "CM", "+237", R.drawable.flag_cameroon, "+237 #### #####"),
    Country("Canada", "CA", "+1", R.drawable.flag_canada, "+1 ### ### ####"),
    Country("Cap-Vert", "CV", "+238", R.drawable.flag_cape_verde, "+238 #######"),
    Country("Îles Caïmans", "KY", "+1 345", R.drawable.flag_cayman_islands, "+1 345 # ### # ###"),
    Country(
        "République centrafricaine",
        "CF",
        "+236",
        R.drawable.flag_central_african_republic,
        "236 ########"
    ),
    Country("Chili", "CL", "+56", R.drawable.flag_chile, "+56 #########"),
    Country("Chine", "CN", "+86", R.drawable.flag_china, "+86 ### #### ####"),
    Country("Chypre", "CY", "+357", R.drawable.flag_cyprus, "+357 ########"),
    Country("Colombie", "CO", "+57", R.drawable.flag_colombia, "+57 ### ### ####"),
    Country("Comores", "KM", "+269", R.drawable.flag_comoros, "+269 ### ####"),
    Country(
        "République démocratique du Congo",
        "CD",
        "+243",
        R.drawable.flag_democratic_republic_of_congo,
        "+243 ## ### ## ##"
    ),
    Country(
        "République du Congo",
        "CG",
        "+242",
        R.drawable.flag_republic_of_the_congo,
        "+242 #########"
    ),
    Country("Îles Cook", "CK", "+682", R.drawable.flag_cook_islands, "+682 ## ###"),
    Country("Corée du Nord", "KP", "+850", R.drawable.flag_north_korea, "+850 # ######"),
    Country("Corée du Sud", "KR", "+82", R.drawable.flag_south_korea, "+82 ## #### ####"),
    Country("Costa Rica", "CR", "+506", R.drawable.flag_costa_rica, "+506 #### ####"),
    Country("Côte d'Ivoire", "CI", "+225", R.drawable.flag_ivory_coast, "+225##########"),
    Country("Croatie", "HR", "+385", R.drawable.flag_croatia, "+385 # ### ####"),
    Country("Cuba", "CU", "+53", R.drawable.flag_cuba, "+53 # #######"),
    Country("Curaçao", "CW", "+599", R.drawable.flag_curacao, "+599 ### ####"),
    Country("Danemark", "DK", "+45", R.drawable.flag_denmark, "+45 ## ## ## ##"),
    Country("Djibouti", "DJ", "+253", R.drawable.flag_djibouti, "+253 ## ######"),
    Country(
        DOMINICAN_REPUBLIC,
        "DO",
        "+1 809",
        R.drawable.flag_dominican_republic,
        "+1 809 ### ####"
    ),
    Country(
        DOMINICAN_REPUBLIC,
        "DO",
        "+1 829",
        R.drawable.flag_dominican_republic,
        "+1 829 ### ####"
    ),
    Country(
        DOMINICAN_REPUBLIC,
        "DO",
        "+1 849",
        R.drawable.flag_dominican_republic,
        "+1 849 ### ####"
    ),
    Country("Dominique", "DM", "+1 767", R.drawable.flag_dominica, "+1 767 ### ####"),
    Country("Égypte", "EG", "+20", R.drawable.flag_egypt, "+20 ## ### ####"),
    Country(
        "Émirats arabes unis",
        "AE",
        "+971",
        R.drawable.flag_united_arab_emirates,
        "+971 ## ### ####"
    ),
    Country("Équateur", "EC", "+593", R.drawable.flag_ecuador, "+593 ## ### ####"),
    Country("Érythrée", "ER", "+291", R.drawable.flag_eritrea, "+291 # ######"),
    Country("Espagne", "ES", "+34", R.drawable.flag_spain, "+34 ### ## ## ##"),
    Country("Estonie", "EE", "+372", R.drawable.flag_estonia, "+372 #### ####"),
    Country("États-Unis", "US", "+1", R.drawable.flag_united_states, "+1 ### ### ####"),
    Country("Éthiopie", "ET", "+251", R.drawable.flag_ethiopia, "+251 ## ### ####"),
    Country("Îles Féroé", "FO", "+298", R.drawable.flag_faroe_islands, "+298 # #####"),
    Country("Fidji", "FJ", "+679", R.drawable.flag_fiji, "+679 #######"),
    Country("Finlande", "FI", "+358", R.drawable.flag_finland, "+358 ### ### ####"),
    Country("France", "FR", "+33", R.drawable.flag_france, "+33 # ## ## ## ##"),
    Country("Gabon", "GA", "+241", R.drawable.flag_gabon, "+241 # ## ## ## ##"),
    Country("Gambie", "GM", "+220", R.drawable.flag_gambia, "+220 #######"),
    Country("Géorgie", "GE", "+995", R.drawable.flag_georgia, "+995 ### ### ###"),
    Country(
        "Géorgie du Sud-et-les îles Sandwich du Sud",
        "GS",
        "+500",
        R.drawable.flag_sandwich_islands,
        "+500 #####"
    ),
    Country("Ghana", "GH", "+233", R.drawable.flag_ghana, "+233 #########"),
    Country("Gibraltar", "GI", "+350", R.drawable.flag_gibraltar, "+350 ########"),
    Country("Grèce", "GR", "+30", R.drawable.flag_greece, "+30 #### ####"),
    Country("Grenade", "GD", "+1 473", R.drawable.flag_grenada, "+1 473 ### ####"),
    Country("Groenland", "GL", "+299", R.drawable.flag_greenland, "+299 ## ####"),
    Country("Guadeloupe", "GP", "+590", R.drawable.flag_france, "+590 ### ## ## ##"),
    Country("Guam", "GU", "+1 671", R.drawable.flag_guam, "+1 671 ### ####"),
    Country("Guatemala", "GT", "+502", R.drawable.flag_guatemala, "+502 ### ### ####"),
    Country("Guernesey", "GG", "+44", R.drawable.flag_guernsey, "+44 #### ######"),
    Country("Guinée", "GN", "+224", R.drawable.flag_guinea, "+244 #########"),
    Country(
        "Guinée équatoriale",
        "GQ",
        "+240",
        R.drawable.flag_equatorial_guinea,
        "+240 ## ######"
    ),
    Country("Guinée-Bissau", "GW", "+245", R.drawable.flag_guinea_bissau, "+245 ########"),
    Country("Guyana", "GY", "+592", R.drawable.flag_guyana, "+592 #######"),
    Country("Guyane", "GF", "+594", R.drawable.flag_france, "+594 ### ## ## ##"),
    Country("Haïti", "HT", "+509", R.drawable.flag_haiti, "+509 #######"),
    Country("Honduras", "HN", "+504", R.drawable.flag_honduras, "+504 #### ####"),
    Country("Hong Kong", "HK", "+852", R.drawable.flag_hong_kong, "+852 #### ####"),
    Country("Hongrie", "HU", "+36", R.drawable.flag_hungary, "+36 # ### ####"),
    Country("Île de Man", "IM", "+44", R.drawable.flag_isle_of_man, "+44 #### ######"),
    Country("Inde", "IN", "+91", R.drawable.flag_india, "+91 ### ### ####"),
    Country("Indonésie", "ID", "+62", R.drawable.flag_indonesia, "+62 ### ### ####"),
    Country("Irak", "IQ", "+964", R.drawable.flag_iraq, "+964 #### -#######"),
    Country("Iran", "IR", "+98", R.drawable.flag_iran, "+98 ### ### ####"),
    Country("Irlande", "IE", "+353", R.drawable.flag_ireland, "+353 ## ### ####"),
    Country("Islande", "IS", "+354", R.drawable.flag_iceland, "+354 ### ####"),
    Country("Israël", "IL", "+972", R.drawable.flag_israel, "+972 ## #######"),
    Country("Italie", "IT", "+39", R.drawable.flag_italy, "+39 ### ### ####"),
    Country("Jamaïque", "JM", "+1 876", R.drawable.flag_jamaica, "+1 876 ### ####"),
    Country("Japon", "JP", "+81", R.drawable.flag_japan, "+81 ## #### ####"),
    Country("Jersey", "JE", "+44", R.drawable.flag_jersey, "44 #### ######"),
    Country("Jordanie", "JO", "+962", R.drawable.flag_jordan, "+962 ## ### ####"),
    Country("Kazakhstan", "KZ", "+7", R.drawable.flag_kazakhstan, "+7 ### ### ####"),
    Country("Kazakhstan", "KZ", "+997", R.drawable.flag_kazakhstan, "+997 ### ### ####"),
    Country("Kenya", "KE", "+254", R.drawable.flag_kenya, "+254 ### #######"),
    Country("Kirghizistan", "KG", "+996", R.drawable.flag_kyrgyzstan, "+996 ### ######"),
    Country("Kiribati", "KI", "+686", R.drawable.flag_kiribati, "+686 #######"),
    Country("Kosovo", "XK", "+383", R.drawable.flag_kosovo, "+383 ## ######"),
    Country("Koweït", "KW", "+965", R.drawable.flag_kwait, "+965 #### ####"),
    Country("Laos", "LA", "+856", R.drawable.flag_laos, "+856 ## #### ####"),
    Country("Lesotho", "LS", "+266", R.drawable.flag_lesotho, "+266 # #######"),
    Country("Lettonie", "LV", "+371", R.drawable.flag_latvia, "+371 ########"),
    Country("Liban", "LB", "+961", R.drawable.flag_lebanon, "+961 # ######"),
    Country("Liberia", "LR", "+231", R.drawable.flag_liberia, "+231 ## ### ####"),
    Country("Libye", "LY", "+218", R.drawable.flag_libya, "+218 ## ### ####"),
    Country("Liechtenstein", "LI", "+423", R.drawable.flag_liechtenstein, "+423 #######"),
    Country("Lituanie", "LT", "+370", R.drawable.flag_lithuania, "+370 #### ####"),
    Country("Luxembourg", "LU", "+352", R.drawable.flag_luxembourg, "+352 ### ### ###"),
    Country("Macao", "MO", "+853", R.drawable.flag_macao, "+853 # ### # ###"),
    Country(
        "Macédoine du Nord",
        "MK",
        "+389",
        R.drawable.flag_republic_of_macedonia,
        "+389 ########"
    ),
    Country("Madagascar", "MG", "+261", R.drawable.flag_madagascar, "+261 ## ########"),
    Country("Malaisie", "MY", "+60", R.drawable.flag_malasya, "+60##########"),
    Country("Malawi", "MW", "+265", R.drawable.flag_malawi, "+265#########"),
    Country("Maldives", "MV", "+960", R.drawable.flag_maldives, "+960 #######"),
    Country("Mali", "ML", "+223", R.drawable.flag_mali, "+223 ########"),
    Country("Malouines", "FK", "+500", R.drawable.flag_falkland_islands, "+500 #####"),
    Country("Malte", "MT", "+356", R.drawable.flag_malta, "+356 ########"),
    Country(
        "Îles Mariannes du Nord",
        "MP",
        "+1670",
        R.drawable.flag_northern_marianas_islands,
        "+1 670 ### ####"
    ),
    Country("Maroc", "MA", "+212", R.drawable.flag_morocco, "+212 ### ### ###"),
    Country("Îles Marshall", "MH", "+692", R.drawable.flag_marshall_island, "+692 ### ####"),
    Country("Martinique", "MQ", "+596", R.drawable.flag_france, "+596 ### ## ####"),
    Country("Maurice", "MU", "+230", R.drawable.flag_mauritius, "+230 #### ####"),
    Country("Mauritanie", "MR", "+222", R.drawable.flag_mauritania, "+222 ########"),
    Country("Mayotte", "YT", "+262", R.drawable.flag_france, "+262########"),
    Country("Mexique", "MX", "+52", R.drawable.flag_mexico, "+52 ## #### ####"),
    Country(
        "États fédérés de Micronésie",
        "FM",
        "+691",
        R.drawable.flag_micronesia,
        "+691 ### ####"
    ),
    Country("Moldavie", "MD", "+373", R.drawable.flag_moldova, "+373 ## ### ####"),
    Country("Monaco", "MC", "+377", R.drawable.flag_monaco, "+377 ## ## ## ##"),
    Country("Mongolie", "MN", "+976", R.drawable.flag_mongolia, "+976 #### ####"),
    Country("Monténégro", "ME", "+382", R.drawable.flag_montenegro, "+382 ## ### ###"),
    Country("Montserrat", "MS", "+1 664", R.drawable.flag_montserrat, "+1 664 ### ####"),
    Country("Mozambique", "MZ", "+258", R.drawable.flag_mozambique, "+258 ## #######"),
    Country("Namibie", "NA", "+264", R.drawable.flag_namibia, "+264 ## #######"),
    Country("Nauru", "NR", "+674", R.drawable.flag_nauru, "+674 ### ####"),
    Country("Népal", "NP", "+977", R.drawable.flag_nepal, "+977 # #######"),
    Country("Nicaragua", "NI", "+505", R.drawable.flag_nicaragua, "+505 #### ####"),
    Country("Niger", "NE", "+227", R.drawable.flag_niger, "+227 ## ### ###"),
    Country("Nigeria", "NG", "+234", R.drawable.flag_nigeria, "+234 ### ### ####"),
    Country("Niue", "NU", "+683", R.drawable.flag_niue, "+683 ####"),
    Country("Île Norfolk", "NF", "+672 3", R.drawable.flag_norfolk_island, "+672 3 #####"),
    Country("Norvège", "NO", "+47", R.drawable.flag_norway, "+47 ########"),
    Country("Nouvelle-Calédonie", "NC", "+687", R.drawable.flag_france, "+687 ### ###"),
    Country("Nouvelle-Zélande", "NZ", "+64", R.drawable.flag_new_zealand, "+64 # #######"),
    Country("Oman", "OM", "+968", R.drawable.flag_oman, "+968 ########"),
    Country("Ouganda", "UG", "+256", R.drawable.flag_uganda, "+256 ### ### ###"),
    Country("Ouzbékistan", "UZ", "+998", R.drawable.flag_uzbekistn, "+998 ## #######"),
    Country("Pakistan", "PK", "+92", R.drawable.flag_pakistan, "+92 ### ### ####"),
    Country("Palaos", "PW", "+680", R.drawable.flag_palau, "+680 ### ####"),
    Country("Palestine", "PS", "+970", R.drawable.flag_palestine, "+970##########"),
    Country("Panama", "PA", "+507", R.drawable.flag_panama, "+507 ### ### ####"),
    Country(
        "Papouasie-Nouvelle-Guinée",
        "PG",
        "+675",
        R.drawable.flag_papua_new_guinea,
        "+675 ### ####"
    ),
    Country("Paraguay", "PY", "+595", R.drawable.flag_paraguay, "+595 ### ### ###"),
    Country("Pays-Bas", "NL", "+31", R.drawable.flag_netherlands, "+31 ### ### ####"),
    Country(
        "Pays-Bas caribéens",
        "BQ",
        "+599",
        R.drawable.flag_caribean_netherlands,
        "+599 ### ####"
    ),
    Country("Pérou", "PE", "+51", R.drawable.flag_peru, "+51 # ### ####"),
    Country("Philippines", "PH", "+63", R.drawable.flag_philippines, "+63 ### ### ####"),
    Country("Îles Pitcairn", "PN", "+64", R.drawable.flag_pitcairn_islands, "+64 ########"),
    Country("Pologne", "PL", "+48", R.drawable.flag_poland, "+48 ## ### ## ##"),
    Country("Polynésie française", "PF", "+689", R.drawable.flag_french_polynesia, "+689 ########"),
    Country("Porto Rico", "PR", "+1 787", R.drawable.flag_puerto_rico, "+1 787 ### ####"),
    Country("Porto Rico", "PR", "+1 939", R.drawable.flag_puerto_rico, "+1 787 ### ####"),
    Country("Portugal", "PT", "+351", R.drawable.flag_portugal, "+351 ## #######"),
    Country("Qatar", "QA", "+974", R.drawable.flag_qatar, "+974 ########"),
    Country("La Réunion", "RE", "+262", R.drawable.flag_france, "+262 ### ## ## ##"),
    Country("Roumanie", "RO", "+40", R.drawable.flag_romania, "+40 ## #######"),
    Country("Royaume-Uni", "GB", "+44", R.drawable.flag_united_kingdom, "+44 ### ### ####"),
    Country("Russie", "RU", "+7", R.drawable.flag_russia, "+7 ### ### ####"),
    Country("Rwanda", "RW", "+250", R.drawable.flag_rwanda, "+250 ### ### ###"),
    Country("Saint-Barthélemy", "BL", "+590", R.drawable.flag_france, "+590 ########"),
    Country(
        "Saint-Christophe-et-Niévès",
        "KN",
        "+1 869",
        R.drawable.flag_saint_kitts_and_nevis,
        "+1 869 ### ####"
    ),
    Country(
        "Sainte-Hélène, Ascension et Tristan da Cunha",
        "SH",
        "+290",
        R.drawable.flag_ascension_island,
        "+290 ######"
    ),
    Country("Sainte-Lucie", "LC", "+ 1758", R.drawable.flag_st_lucia, "+1 758 ### ####"),
    Country("Saint-Marin", "SM", "+378", R.drawable.flag_san_marino, "+378 #### ####"),
    Country("Saint-Martin", "MF", "+590", R.drawable.flag_france, "+690 ## ## ##"),
    Country("Saint-Pierre-et-Miquelon", "PM", "+508", R.drawable.flag_france, "+508######"),
    Country(
        "Saint-Vincent-et-les-Grenadines",
        "VC",
        "+1 784",
        R.drawable.flag_st_vincent_and_the_grenadines,
        "+1 784 ### ####"
    ),
    Country("Îles Salomon", "SB", "+677", R.drawable.flag_solomon_islands, "+677 ### ####"),
    Country("Salvador", "SV", "+503", R.drawable.flag_el_salvador, "+503 #### ####"),
    Country("Samoa", "WS", "+685", R.drawable.flag_samoa, "+685 ### ####"),
    Country("Samoa américaines", "AS", "+1 684", R.drawable.flag_american_samoa, "+1 684 ### ####"),
    Country(
        "Sao Tomé-et-Principe",
        "ST",
        "+239",
        R.drawable.flag_sao_tome_and_prince,
        "+239 ### ####"
    ),
    Country("Sénégal", "SN", "+221", R.drawable.flag_senegal, "+221 ## ### ####"),
    Country("Serbie", "RS", "+381", R.drawable.flag_serbia, "+381 ### ### ###"),
    Country("Seychelles", "SC", "+248", R.drawable.flag_seychelles, "+248 ### ####"),
    Country("Sierra Leone", "SL", "+232", R.drawable.flag_sierra_leone, "+232 ## ######"),
    Country("Singapour", "SG", "+65", R.drawable.flag_singapore, "+65 #### ####"),
    Country("Saint-Martin", "SX", "+1 721", R.drawable.flag_saint_martin, "+1 721 ### ####"),
    Country("Slovaquie", "SK", "+421", R.drawable.flag_slovakia, "+421 ### ### ###"),
    Country("Slovénie", "SI", "+386", R.drawable.flag_slovenia, "+386 ### ### ###"),
    Country("Somalie", "SO", "+252", R.drawable.flag_somalia, "+252 ## ### ####"),
    Country("Soudan", "SD", "+249", R.drawable.flag_sudan, "+249#########"),
    Country("Soudan du Sud", "SS", "+211", R.drawable.flag_south_sudan, "+211 ### #######"),
    Country("Sri Lanka", "LK", "+94", R.drawable.flag_sri_lanka, "+94#########"),
    Country("Suède", "SE", "+46", R.drawable.flag_sweden, "+46 ### ###"),
    Country("Suisse", "CH", "+41", R.drawable.flag_switzerland, "+41 ## ### ## ##"),
    Country("Suriname", "SR", "+597", R.drawable.flag_suriname, "+597 #######"),
    Country("Svalbard", "SJ", "+47", R.drawable.flag_norway, "+47 #########"),
    Country("Eswatini", "SZ", "+268", R.drawable.flag_swaziland, "+268 ## ## ####"),
    Country("Syrie", "SY", "+963", R.drawable.flag_syria, "+963 ### #######"),
    Country("Tadjikistan", "TJ", "+992", R.drawable.flag_tajikistan, "+992 #### ######"),
    Country("Tanzanie", "TZ", "+255", R.drawable.flag_tanzania, "+255 ### ### ###"),
    Country("Taïwan", "TW", "+886", R.drawable.flag_taiwan, "+886 #### ####"),
    Country("Tchad", "TD", "+235", R.drawable.flag_chad, "+235 ## ## ## ##"),
    Country("République tchèque", "CZ", "+420", R.drawable.flag_czech_republic, "+420 ### ### ###"),
    Country(
        "Terres australes et antarctiques françaises",
        "TF",
        "+262",
        R.drawable.flag_french_southern_territories,
        "+262 #### ####"
    ),
    Country(
        "Territoire britannique de l'océan Indien",
        "IO",
        "+246",
        R.drawable.flag_british_indian_ocean_territory,
        "+246 #######"
    ),
    Country("Île Christmas", "CX", "+61", R.drawable.flag_christmas_island, "+61 #########"),
    Country("Îles Cocos", "CC", "+61", R.drawable.flag_cocos_island, "+61 # #### ####"),
    Country("Îles Heard-et-MacDonald", "HM", "+672", R.drawable.flag_australia, "+672 ### ###"),
    Country("Thaïlande", "TH", "+66", R.drawable.flag_thailand, "+66 ## ### ####"),
    Country("Timor oriental", "TL", "+670", R.drawable.flag_east_timor, "+670 #######"),
    Country("Togo", "TG", "+228", R.drawable.flag_togo, "+228 ## ## ## ##"),
    Country("Tokelau", "TK", "+690", R.drawable.flag_tokelau, "+690 ########"),
    Country("Tonga", "TO", "+676", R.drawable.flag_tonga, "+676 ### ####"),
    Country(
        "Trinité-et-Tobago",
        "TT",
        "+1 868",
        R.drawable.flag_trinidad_and_tobago,
        "+1 868 ### ####"
    ),
    Country("Tunisie", "TN", "+216", R.drawable.flag_tunisia, "+216 ## ### ###"),
    Country("Turkménistan", "TM", "+993", R.drawable.flag_turkmenistan, "+993 ## ######"),
    Country(
        "Îles Turques-et-Caïques",
        "TC",
        "+1 649",
        R.drawable.flag_turks_and_caicos,
        "+1 649 ### ####"
    ),
    Country("Turquie", "TR", "+90", R.drawable.flag_turkey, "+90 ### ### ####"),
    Country("Tuvalu", "TV", "+688", R.drawable.flag_tuvalu, "+688 #### ####"),
    Country("Ukraine", "UA", "+380", R.drawable.flag_ukraine, "+380 ## ### ####"),
    Country("Uruguay", "UY", "+598", R.drawable.flag_uruguay, "+598 ########"),
    Country("Vanuatu", "VU", "+678", R.drawable.flag_vanuatu, "+678 ## ### ###"),
    Country("Vatican", "VA", "+39", R.drawable.flag_vatican_city, "+39 06 698 #####"),
    Country("Venezuela", "VE", "+58", R.drawable.flag_venezuela, "+58 ### #######"),
    Country(
        "Îles Vierges des États-Unis",
        "VI",
        "+1 340",
        R.drawable.flag_us_virgin_islands,
        "+1 340 ### ####"
    ),
    Country(
        "Îles Vierges britanniques",
        "VG",
        "+1 284",
        R.drawable.flag_british_virgin_islands,
        "+1 284 ### ####"
    ),
    Country("Viêt Nam", "VN", "+84", R.drawable.flag_vietnam, "+84 ### ### ###"),
    Country("Wallis-et-Futuna", "WF", "+681", R.drawable.flag_wallis_and_fortune, "+681 ######"),
    Country("Yémen", "YE", "+967", R.drawable.flag_yemen, "+967 # #######"),
    Country("Zambie", "ZM", "+260", R.drawable.flag_zambia, "+260 ## ### ####"),
    Country("Zimbabwe", "ZW", "+263", R.drawable.flag_zimbabwe, "+263 ## #######"),
    Country(
        "Systèmes mobiles mondiaux par satellite GMSS",
        "",
        "+881",
        R.drawable.flag_white,
        "+881 # ### ####"
    ),
    Country(
        "Systèmes mobiles mondiaux par satellite GMSS",
        "",
        "+882",
        R.drawable.flag_white,
        "+882 ## ### ####"
    ),
    Country("Numéros internationaux", "", "+883", R.drawable.flag_white, "+883 ## ### ####"),
    Country(
        "Numéro gratuit et universel de libre appel international",
        "",
        "+6039",
        R.drawable.flag_white,
        "+6039 #### ####"
    ),
)
