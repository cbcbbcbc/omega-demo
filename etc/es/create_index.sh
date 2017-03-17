HTTP="http://localhost:9200"

echo "`date` Delete index orderform_20170314..."
curl -XDELETE $HTTP/orderform_20170314
echo

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
                "userId": {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "number": {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "amount": {
                    "type": "integer"
                },
                "gmtCreated": {
                    "type": "date"
                },
                "itemNo": {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "itemName": {
                    "type": "string",
                    "index": "analyzed"
                }
            }
        }
   }
}'
echo

echo "`date` Create index alias orderform..."
curl -XPUT $HTTP/orderform_20170314/_alias/orderform
echo



