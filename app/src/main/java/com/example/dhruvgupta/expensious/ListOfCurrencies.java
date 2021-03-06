package com.example.dhruvgupta.expensious;

import java.util.ArrayList;

/**
 * Created by dhruvgupta on 3/26/2015.
 */
public class ListOfCurrencies
{
    CurrencyDB c[];
    ArrayList<CurrencyDB> al;

    ArrayList<CurrencyDB> getAllCurrencies() {
        c = new CurrencyDB[112];
        al = new ArrayList<>();

        for (int i=0; i<112; i++)
            c[i]=new CurrencyDB();

        c[0].c_name = "Albania";
        c[0].c_code = "ALL";
        c[0].c_symbol = "Lek";
        al.add(c[0]);
        c[1].c_name = "Afghanistan";
        c[1].c_code = "AFN";
        c[1].c_symbol = "؋";
        al.add(c[1]);
        c[2].c_name = "Argentina";
        c[2].c_code = "ARS";
        c[2].c_symbol = "$";
        al.add(c[2]);
        c[3].c_name = "Aruba";
        c[3].c_code = "AWG";
        c[3].c_symbol = "ƒ";
        al.add(c[3]);
        c[4].c_name = "Australia";
        c[4].c_code = "AUD";
        c[4].c_symbol = "$";
        al.add(c[4]);
        c[5].c_name = "Azerbaijan";
        c[5].c_code = "AZN";
        c[5].c_symbol = "ман";
        al.add(c[5]);
        c[6].c_name = "Bahamas";
        c[6].c_code = "BSD";
        c[6].c_symbol = "$";
        al.add(c[6]);
        c[7].c_name = "Barbados";
        c[7].c_code = "BBD";
        c[7].c_symbol = "$";
        al.add(c[7]);
        c[8].c_name = "Belarus";
        c[8].c_code = "BYR";
        c[8].c_symbol = "p.";
        al.add(c[8]);
        c[9].c_name = "Belize";
        c[9].c_code = "BZD";
        c[9].c_symbol = "BZ$";
        al.add(c[9]);
        c[10].c_name = "Bermuda";
        c[10].c_code = "BMD";
        c[10].c_symbol = "$";
        al.add(c[10]);
        c[11].c_name = "Bolivia";
        c[11].c_code = "BOB";
        c[11].c_symbol = "$b";
        al.add(c[11]);
        c[12].c_name = "Bosnia and Herzegovina";
        c[12].c_code = "BAM";
        c[12].c_symbol = "KM";
        al.add(c[12]);
        c[13].c_name = "Botswana";
        c[13].c_code = "BWP";
        c[13].c_symbol = "P";
        al.add(c[13]);
        c[14].c_name = "Bulgaria";
        c[14].c_code = "BGN";
        c[14].c_symbol = "лв";
        al.add(c[14]);
        c[15].c_name = "Brazil";
        c[15].c_code = "BRL";
        c[15].c_symbol = "R$";
        al.add(c[15]);
        c[16].c_name = "Brunei";
        c[16].c_code = "BND";
        c[16].c_symbol = "$";
        al.add(c[16]);
        c[17].c_name = "Cambodia";
        c[17].c_code = "KHR";
        c[17].c_symbol = "៛";
        al.add(c[18]);
        c[18].c_name = "Canada";
        c[18].c_code = "CAD";
        c[18].c_symbol = "$";
        al.add(c[18]);
        c[19].c_name = "Cayman";
        c[19].c_code = "KYD";
        c[19].c_symbol = "$";
        al.add(c[19]);
        c[20].c_name = "Chile";
        c[20].c_code = "CLP";
        c[20].c_symbol = "$";
        al.add(c[20]);
        c[21].c_name = "China";
        c[21].c_code = "CNY";
        c[21].c_symbol = "¥";
        al.add(c[21]);
        c[22].c_name = "Colombia";
        c[22].c_code = "COP";
        c[22].c_symbol = "$";
        al.add(c[22]);
        c[23].c_name = "Costa Rica";
        c[23].c_code = "CRC";
        c[23].c_symbol = "₡";
        al.add(c[23]);
        c[24].c_name = "Croatia";
        c[24].c_code = "HRK";
        c[24].c_symbol = "kn";
        al.add(c[24]);
        c[25].c_name = "Cuba";
        c[25].c_code = "CUP";
        c[25].c_symbol = "₱";
        al.add(c[25]);
        c[26].c_name = "Czech Republic";
        c[26].c_code = "CZK";
        c[26].c_symbol = "Kč";
        al.add(c[26]);
        c[27].c_name = "Denmark";
        c[27].c_code = "DKK";
        c[27].c_symbol = "kr";
        al.add(c[27]);
        c[28].c_name = "Dominican Republic";
        c[28].c_code = "DOP";
        c[28].c_symbol = "RD$";
        al.add(c[28]);
        c[29].c_name = "East Caribbean";
        c[29].c_code = "XCD";
        c[29].c_symbol = "$";
        al.add(c[29]);
        c[30].c_name = "Egypt";
        c[30].c_code = "EGP";
        c[30].c_symbol = "£";
        al.add(c[30]);
        c[31].c_name = "El Salvador";
        c[31].c_code = "SVC";
        c[31].c_symbol = "$";
        al.add(c[31]);
        c[32].c_name = "Estonia";
        c[32].c_code = "EEK";
        c[32].c_symbol = "kr";
        al.add(c[32]);
        c[33].c_name = "Euro Member";
        c[33].c_code = "EUR";
        c[33].c_symbol = "€";
        al.add(c[33]);
        c[34].c_name = "Falkland Islands";
        c[34].c_code = "FKP";
        c[34].c_symbol = "£";
        al.add(c[34]);
        c[35].c_name = "Fiji";
        c[35].c_code = "FJD";
        c[35].c_symbol = "$";
        al.add(c[35]);
        c[36].c_name = "Ghana";
        c[36].c_code = "GHC";
        c[36].c_symbol = "¢";
        al.add(c[36]);
        c[37].c_name = "Gibraltar";
        c[37].c_code = "GIP";
        c[37].c_symbol = "£";
        al.add(c[37]);
        c[38].c_name = "Guatemala";
        c[38].c_code = "GTQ";
        c[38].c_symbol = "Q";
        al.add(c[38]);
        c[39].c_name = "Guernsey";
        c[39].c_code = "GGP";
        c[39].c_symbol = "£";
        al.add(c[39]);
        c[40].c_name = "Guyana";
        c[40].c_code = "GYD";
        c[40].c_symbol = "$";
        al.add(c[40]);
        c[41].c_name = "Honduras";
        c[41].c_code = "HNL";
        c[41].c_symbol = "L";
        al.add(c[41]);
        c[42].c_name = "Hong Kong";
        c[42].c_code = "HKD";
        c[42].c_symbol = "$";
        al.add(c[42]);
        c[43].c_name = "Hungary";
        c[43].c_code = "HUF";
        c[43].c_symbol = "Ft";
        al.add(c[43]);
        c[44].c_name = "Iceland";
        c[44].c_code = "ISK";
        c[44].c_symbol = "kr";
        al.add(c[44]);
        c[45].c_name = "India";
        c[45].c_code = "INR";
        c[45].c_symbol = "₹";
        al.add(c[45]);
        c[46].c_name = "Indonesia";
        c[46].c_code = "IDR";
        c[46].c_symbol = "Rp";
        al.add(c[46]);
        c[47].c_name = "Iran";
        c[47].c_code = "IRR";
        c[47].c_symbol = "﷼";
        al.add(c[47]);
        c[48].c_name = "Isle of Man";
        c[48].c_code = "IMP";
        c[48].c_symbol = "£";
        al.add(c[48]);
        c[49].c_name = "Israel";
        c[49].c_code = "ILS";
        c[49].c_symbol = "₪";
        al.add(c[49]);
        c[50].c_name = "Jamaica";
        c[50].c_code = "JMD";
        c[50].c_symbol = "J$";
        al.add(c[50]);
        c[51].c_name = "Japan";
        c[51].c_code = "JPY";
        c[51].c_symbol = "¥";
        al.add(c[51]);
        c[52].c_name = "Jersey";
        c[52].c_code = "JEP";
        c[52].c_symbol = "£";
        al.add(c[52]);
        c[53].c_name = "Kazakhstan";
        c[53].c_code = "KZT";
        c[53].c_symbol = "лв";
        al.add(c[53]);
        c[54].c_name = "Korea (North)";
        c[54].c_code = "KPW";
        c[54].c_symbol = "₩";
        al.add(c[54]);
        c[55].c_name = "Korea (South)";
        c[55].c_code = "KRW";
        c[55].c_symbol = "₩";
        al.add(c[55]);
        c[56].c_name = "Kyrgyzstan";
        c[56].c_code = "KGS";
        c[56].c_symbol = "лв";
        al.add(c[56]);
        c[57].c_name = "Laos";
        c[57].c_code = "LAK";
        c[57].c_symbol = "₭";
        al.add(c[57]);
        c[58].c_name = "Latvia";
        c[58].c_code = "LVL";
        c[58].c_symbol = "Ls";
        al.add(c[58]);
        c[59].c_name = "Lebanon";
        c[59].c_code = "LBP";
        c[59].c_symbol = "£";
        al.add(c[59]);
        c[60].c_name = "Liberia";
        c[60].c_code = "LRD";
        c[60].c_symbol = "$";
        al.add(c[60]);
        c[61].c_name = "Lithuania";
        c[61].c_code = "LTL";
        c[61].c_symbol = "Lt";
        al.add(c[61]);
        c[62].c_name = "Macedonia";
        c[62].c_code = "MKD";
        c[62].c_symbol = "ден";
        al.add(c[62]);
        c[63].c_name = "Malaysia";
        c[63].c_code = "MYR";
        c[63].c_symbol = "RM";
        al.add(c[63]);
        c[64].c_name = "Mauritius";
        c[64].c_code = "MUR";
        c[64].c_symbol = "Rs";
        al.add(c[64]);
        c[65].c_name = "Mexico";
        c[65].c_code = "MXN";
        c[65].c_symbol = "$";
        al.add(c[65]);
        c[66].c_name = "Mongolia";
        c[66].c_code = "MNT";
        c[66].c_symbol = "₮";
        al.add(c[66]);
        c[67].c_name = "Mozambique";
        c[67].c_code = "MZN";
        c[67].c_symbol = "MT";
        al.add(c[67]);
        c[68].c_name = "Namibia";
        c[68].c_code = "NAD";
        c[68].c_symbol = "$";
        al.add(c[68]);
        c[69].c_name = "Nepal";
        c[69].c_code = "NPR";
        c[69].c_symbol = "Rs";
        al.add(c[69]);
        c[70].c_name = "Netherlands";
        c[70].c_code = "ANG";
        c[70].c_symbol = "ƒ";
        al.add(c[70]);
        c[71].c_name = "New Zealand";
        c[71].c_code = "NZD";
        c[71].c_symbol = "$";
        al.add(c[71]);
        c[72].c_name = "Nicaragua";
        c[72].c_code = "NIO";
        c[72].c_symbol = "C$";
        al.add(c[72]);
        c[73].c_name = "Nigeria";
        c[73].c_code = "NGN";
        c[73].c_symbol = "₦";
        al.add(c[73]);
        c[74].c_name = "Norway";
        c[74].c_code = "NOK";
        c[74].c_symbol = "kr";
        al.add(c[74]);
        c[75].c_name = "Oman";
        c[75].c_code = "OMR";
        c[75].c_symbol = "﷼";
        al.add(c[75]);
        c[76].c_name = "Pakistan";
        c[76].c_code = "PKR";
        c[76].c_symbol = "Rs";
        al.add(c[76]);
        c[77].c_name = "Panama";
        c[77].c_code = "PAB";
        c[77].c_symbol = "B/.";
        al.add(c[77]);
        c[78].c_name = "Paraguay";
        c[78].c_code = "PYG";
        c[78].c_symbol = "Gs";
        al.add(c[78]);
        c[79].c_name = "Peru";
        c[79].c_code = "PEN";
        c[79].c_symbol = "S/.";
        al.add(c[79]);
        c[80].c_name = "Philippines";
        c[80].c_code = "PHP";
        c[80].c_symbol = "₱";
        al.add(c[80]);
        c[81].c_name = "Poland";
        c[81].c_code = "PLN";
        c[81].c_symbol = "zł";
        al.add(c[81]);
        c[82].c_name = "Qatar";
        c[82].c_code = "QAR";
        c[82].c_symbol = "﷼";
        al.add(c[82]);
        c[83].c_name = "Romania";
        c[83].c_code = "RON";
        c[83].c_symbol = "lei";
        al.add(c[83]);
        c[84].c_name = "Russia";
        c[84].c_code = "RUB";
        c[84].c_symbol = "руб";
        al.add(c[84]);
        c[85].c_name = "Saint Helena";
        c[85].c_code = "SHP";
        c[85].c_symbol = "£";
        al.add(c[85]);
        c[86].c_name = "Saudi Arabia";
        c[86].c_code = "SAR";
        c[86].c_symbol = "﷼";
        al.add(c[86]);
        c[87].c_name = "Serbia";
        c[87].c_code = "RSD";
        c[87].c_symbol = "Дин.";
        al.add(c[87]);
        c[88].c_name = "Seychelles";
        c[88].c_code = "SCR";
        c[88].c_symbol = "Rs";
        al.add(c[88]);
        c[89].c_name = "Singapore";
        c[89].c_code = "SGD";
        c[89].c_symbol = "$";
        al.add(c[89]);
        c[90].c_name = "Solomon Islands";
        c[90].c_code = "SBD";
        c[90].c_symbol = "$";
        al.add(c[90]);
        c[91].c_name = "Somalia";
        c[91].c_code = "SOS";
        c[91].c_symbol = "S";
        al.add(c[91]);
        c[92].c_name = "South Africa";
        c[92].c_code = "ZAR";
        c[92].c_symbol = "S";
        al.add(c[92]);
        c[93].c_name = "Sri Lanka";
        c[93].c_code = "LKR";
        c[93].c_symbol = "Rs";
        al.add(c[93]);
        c[94].c_name = "Sweden";
        c[94].c_code = "SEK";
        c[94].c_symbol = "kr";
        al.add(c[94]);
        c[95].c_name = "Switzerland";
        c[95].c_code = "CHF";
        c[95].c_symbol = "CHF";
        al.add(c[95]);
        c[96].c_name = "Suriname";
        c[96].c_code = "SRD";
        c[96].c_symbol = "$";
        al.add(c[96]);
        c[97].c_name = "Syria";
        c[97].c_code = "SYP";
        c[97].c_symbol = "£";
        al.add(c[97]);
        c[98].c_name = "Taiwan";
        c[98].c_code = "TWD";
        c[98].c_symbol = "NT$";
        al.add(c[98]);
        c[99].c_name = "Thailand";
        c[99].c_code = "THB";
        c[99].c_symbol = "฿";
        al.add(c[99]);
        c[100].c_name = "Trinidad and Tobago";
        c[100].c_code = "TTD";
        c[100].c_symbol = "TT$";
        al.add(c[100]);
        c[101].c_name = "Turkey";
        c[101].c_code = "TRL";
        c[101].c_symbol = "₤";
        al.add(c[101]);
        c[102].c_name = "Tuvalu";
        c[102].c_code = "TVD";
        c[102].c_symbol = "$";
        al.add(c[102]);
        c[103].c_name = "Ukraine";
        c[103].c_code = "UAH";
        c[103].c_symbol = "₴";
        al.add(c[103]);
        c[104].c_name = "United Kingdom";
        c[104].c_code = "GBP";
        c[104].c_symbol = "£";
        al.add(c[104]);
        c[105].c_name = "United States";
        c[105].c_code = "USD";
        c[105].c_symbol = "$";
        al.add(c[105]);
        c[106].c_name = "Uruguay";
        c[106].c_code = "UYU";
        c[106].c_symbol = "$U";
        al.add(c[106]);
        c[107].c_name = "Uzbekistan";
        c[107].c_code = "UZS";
        c[107].c_symbol = "лв";
        al.add(c[107]);
        c[108].c_name = "Venezuela";
        c[108].c_code = "VEF";
        c[108].c_symbol = "Bs";
        al.add(c[108]);
        c[109].c_name = "Viet Nam";
        c[109].c_code = "VND";
        c[109].c_symbol = "₫";
        al.add(c[109]);
        c[110].c_name = "Yemen";
        c[110].c_code = "YER";
        c[110].c_symbol = "﷼";
        al.add(c[110]);
        c[111].c_name = "Zimbabwe";
        c[111].c_code = "ZWD";
        c[111].c_symbol = "Z$";
        al.add(c[111]);

        return al;
    }
}
