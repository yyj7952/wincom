IP=localhost
SA_ID=M12082900251
STB_MAC=001c.627a.48be

curl -X POST "http://${IP}/authorizeCTVMView?access_key=CEFB8825FFD5948988A9&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&svc_type=1&svc_id=1018&cha_no=303&cat_id=CU00A&con_id=M01156B384PPV00HD100&request_type=1&app_type=CUTA&defin_flag=1&net_type=02&dev_model=TVG&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/authorizeCTVSView?access_key=C7BA63F2966BC4AEBBDE&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&svc_type=1&svc_id=1018&cha_no=303&cat_id=CU00A&con_id=M01156B384PPV00HD100&request_type=1&app_type=CUTA&defin_flag=1&net_type=02&dev_model=TVG&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/getCTVContEpisode?access_key=C98C74F32EDCB496F9ED&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&cat_id=CU00A&series_no=1&defin_flag=1&net_type=02&view_cnt=10&dev_model&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/getCTVThumbnail?access_key=C844B092958B44691B73&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&con_id=M01156B384PPV00HD100&dev_model&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/buyCTVContent?access_key=C5A09D8AD31014A54AF6&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&pkg_yn=N&con_id=M01156N085PPV00HD100&con_name=ShowMeTheMoney4&cat_id=CU00A&buying_price=1500&buying_type=B&app_type=CUTA&ctv_buy_yn=Y&dev_model&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/getCTVPurchased?access_key=C433EC8A1071245FB997&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&page_no=1&page_cnt=20&cat_gb=CTV&app_type=CUTA&quick_dis_yn=N&order_gb=S&net_type=02&defin_flag=1&dev_model&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/getCTVReposited?access_key=C645336B28E274CE297D&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&page_no=1&page_cnt=20&r_grade=A&cat_gb=CTV&quick_dis_yn=N&net_type=02&defin_flag=1&dev_model&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/rmCTVWatchHist?access_key=CF354070770F5473B870&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&con_id=M01156B384PPV00HD100&dev_model&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/setCTVPassedTime?access_key=CEA5AA85DDDB64EB3947&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&cat_id=CU00A&con_id=M01156N085PPV00HD100&link_time=245&buying_date=20140124134017&run_time=360&key_log=03&app_type=CUTA&prev_flag=1&cha_no=1&request_type=1&begin_time=20150427151432&dev_model&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl -X POST "http://${IP}/getCTVContStat?access_key=CE86C07E9ACDE4F84A51&cp_id=4001976995&sa_id=${SA_ID}&stb_mac=${STB_MAC}&con_id=M01156N085PPV00HD100&dev_model&client_ip&dev_info&os_info&nw_info&carrier_type"
echo ''
echo '--------------------------------------------------------------------------'
echo ''
sleep 3

curl "http://${IP}/chkCTVBuyConts?SA_ID=${SA_ID}&STB_MAC=${STB_MAC}&CONTS_ID=M01156N085PPV00HD100"
echo ''
echo '--------------------------------------------------------------------------'
echo ''

