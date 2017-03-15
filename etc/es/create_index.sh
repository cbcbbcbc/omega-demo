HTTP="http://localhost:9200"

#echo "`date` Delete index orderform_20170314..."
#curl -XDELETE $HTTP/orderform_20170314
#echo

echo "`date` Create index orderform_20170314..."
curl -XPUT $HTTP/orderform_20170314 -d '{
   "settings": {
      "refresh_interval" : "30s"
   },

   "mapping": {
        "orderform": {
            "dynamic": "strict",
            "_all": { "enabled": false },
            "_source": { "enabled": false },
            "properties": {
                "id": {
                    "type": "keyword",
                    "store": true
                },
                "userId": {
                    "type": "keyword"
                },
                "number": {
                    "type": "keyword"
                },
                "amount": {
                    "type": "scaled_float",
                    "scaling_factor": 100
                },
                "gmtCreated": {
                    "type": "date"
                },
                "itemNo": {
                    "type": "keyword"
                },
                "itemName": {
                    "type": "text"
                }
            }
        }
   }
}'
echo

echo "`date` Create index alias orderform..."
curl -XPUT $HTTP/orderform_20170314/_alias/orderform
echo



