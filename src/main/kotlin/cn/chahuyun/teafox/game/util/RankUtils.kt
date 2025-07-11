package cn.chahuyun.teafox.game.util

import cn.chahuyun.teafox.game.Card
import cn.chahuyun.teafox.game.CardRank
import cn.chahuyun.teafox.game.util.CardUtil.get
import cn.chahuyun.teafox.game.util.CardUtil.toGroup

/**
 * 牌面值工具
 */
object RankUtils {
    /**
     * 按牌面值分组（忽略花色）
     */
    fun groupByRank(cards: List<Card>): Map<CardRank, List<Card>> {
        return cards.groupBy { it.rank }
    }

    /**
     * 检查是否可以出牌（基于牌面值）
     */
    fun canPlayCards(hand: List<Card>, toPlay: List<Card>): Boolean {
        return toPlay.toGroup().all { it->
            (hand.toGroup().get(it)?.num ?: 0) >= it.num
        }
    }

    /**
     * 从手牌中移除指定的牌
     */
    fun removeCards(hand: MutableList<Card>, toRemove: List<Card>): Boolean {
        if (!canPlayCards(hand, toRemove)) return false

        toRemove.forEach { cardToRemove ->
            // 找到第一个匹配牌面值的牌（忽略花色）
            val index = hand.indexOfFirst { it.rank == cardToRemove.rank }
            if (index != -1) hand.removeAt(index)
        }
        return true
    }
}