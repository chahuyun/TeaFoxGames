package cn.chahuyun.teafox.game

import cn.chahuyun.authorize.EventComponent
import cn.chahuyun.authorize.MessageAuthorize
import cn.chahuyun.authorize.utils.MessageUtil.sendMessageQuery
import cn.chahuyun.hibernateplus.HibernateFactory
import cn.chahuyun.teafox.game.data.FoxUser
import cn.chahuyun.teafox.game.util.MessageUtil
import kotlinx.coroutines.delay
import net.mamoe.mirai.event.events.GroupMessageEvent

@EventComponent
class FundingManger {

    companion object {

        suspend fun getHyGold(user: FoxUser, coins: Int): Boolean {
            val friend = TeaFoxGames.bot.getFriend(DZConfig.economyBot)?: return false

            friend.sendMessage("#fund get ${user.funding} $coins")

            val message = MessageUtil.nextMessage(DZConfig.economyBot)?.message?.contentToString() ?: run {
                return false
            }

            if (message.matches(Regex("^fund get \\S+ \\d+ success"))) {

            }

            return false
        }

    }


    @MessageAuthorize(text = ["#fund bind"])
    suspend fun fundBind(event: GroupMessageEvent) {
        val user = FoxUserManager.getFoxUser(event.sender)

        if (user.funding != null) {
            event.sendMessageQuery("你的账户已经绑定了经济账户!")
            return
        }


        val friend = TeaFoxGames.bot.getFriend(DZConfig.economyBot)

        if (friend == null) {
            event.sendMessageQuery("系统未找到经济账户!")
            return
        }

        friend.sendMessage("#fund bind ${user.uid}")
        delay(1000)

        val msg = MessageUtil.nextMessage(DZConfig.economyBot)?.message?.contentToString() ?: run {
            event.sendMessageQuery("绑定失败!")
            return
        }

        if (msg.matches(Regex("^fund bind \\S+ \\S+"))) {
            val qq = msg.split(" ")[1]
            val uuid = msg.split(" ")[2]

            val bindUser = FoxUserManager.getFoxUser(qq.toLong()) ?: run {
                event.subject.sendMessage("$qq 用户不存在!")
                return
            }
            bindUser.funding = uuid
            HibernateFactory.merge(bindUser)
            event.subject.sendMessage("用户 $qq 绑定成功!")
        }
    }

}