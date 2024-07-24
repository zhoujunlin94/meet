local result = redis.call('zrangebyscore', KEYS[1], 0, ARGV[1], 'LIMIT', 0, 1)
if next(result) ~= nil and #result > 0 then
   local re = redis.call('zrem', KEYS[1], unpack(result));
   if re > 0 then
       return result;
   end
   return {}
else
   return {}
end