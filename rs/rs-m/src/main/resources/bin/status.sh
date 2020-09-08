  #avail_ip=`/sbin/ifconfig $nic 2> /dev/null | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}'|head -1`
  #url="http://$avail_ip:6600/rsm"
  url="http://127.0.0.1:6600/rsm"
  server_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} "$url"`
  if [ "$server_status_code" = "404" -o "$server_status_code" = "000" ]; then 
        echo "stoped"
        exit 0
  else 
       echo "started" 
       exit 0
  fi
