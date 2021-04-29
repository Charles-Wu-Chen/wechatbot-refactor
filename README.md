# wechatbot-refactor
Refactor wechatbot project with Clean/Hexagonal architeture based on the book "Get Your Hands Dirty on Clean Architecture" by @TomHombergs.  

## API Test
```
curl -v -X POST localhost:8080/messages -H 'Content-Type:application/json' -d '{"toID": "wxid_1194601945911", "content": "gardener", "messageType":"Text", "messageChannel": "Person"}'
```
./heartbeat.sh > heartbeat.log 2>&1 &
