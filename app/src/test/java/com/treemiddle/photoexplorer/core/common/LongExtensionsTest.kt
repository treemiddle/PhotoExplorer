package com.treemiddle.photoexplorer.core.common

import com.treemiddle.photoexplorer.core.common.formatCount
import org.junit.Assert.assertEquals
import org.junit.Test

class LongExtensionsTest {
    @Test
    fun `1000 미만의 수가 주어졌을 때_formatCount()를 호출하면_원래 숫자를 그대로 반환한다`() {
        // given
        val count = 999L

        // when
        val result = count.formatCount()

        // then
        assertEquals("999", result)
    }

    @Test
    fun `0이 주어졌을 때_formatCount()를 호출하면_0을 반환한다`() {
        // given
        val count = 0L

        // when
        val result = count.formatCount()

        // then
        assertEquals("0", result)
    }

    @Test
    fun `정확히 1000이 주어졌을 때_formatCount()를 호출하면_소수부 없이 1K 를 반환한다`() {
        // given
        val count = 1_000L

        // when
        val result = count.formatCount()

        // then
        assertEquals("1K", result)
    }

    @Test
    fun `소수부가 있는 천 단위 수가 주어졌을 때_formatCount()를 호출하면_소수 첫째자리까지 K 로 반환한다`() {
        // given
        val count = 1_234L

        // when
        val result = count.formatCount()

        // then
        assertEquals("1.2K", result)
    }

    @Test
    fun `1000의 배수인 천 단위 수가 주어졌을 때_formatCount()를 호출하면_소수부 없이 K 로 반환한다`() {
        // given
        val count = 12_000L

        // when
        val result = count.formatCount()

        // then
        assertEquals("12K", result)
    }

    @Test
    fun `백만 직전의 수가 주어졌을 때_formatCount()를 호출하면_K 단위로 반환한다`() {
        // given
        val count = 999_999L

        // when
        val result = count.formatCount()

        // then
        assertEquals("1000.0K", result)
    }

    @Test
    fun `정확히 백만이 주어졌을 때_formatCount()를 호출하면_소수부 없이 1M 을 반환한다`() {
        // given
        val count = 1_000_000L

        // when
        val result = count.formatCount()

        // then
        assertEquals("1M", result)
    }

    @Test
    fun `소수부가 있는 백만 단위 수가 주어졌을 때_formatCount()를 호출하면_소수 첫째자리까지 M 으로 반환한다`() {
        // given
        val count = 1_234_567L

        // when
        val result = count.formatCount()

        // then
        assertEquals("1.2M", result)
    }

    @Test
    fun `백만의 배수인 수가 주어졌을 때_formatCount()를 호출하면_소수부 없이 M 으로 반환한다`() {
        // given
        val count = 3_000_000L

        // when
        val result = count.formatCount()

        // then
        assertEquals("3M", result)
    }
}