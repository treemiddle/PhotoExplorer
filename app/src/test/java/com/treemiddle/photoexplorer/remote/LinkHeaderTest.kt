package com.treemiddle.photoexplorer.remote

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LinkHeaderTest {
    private val full =
        """<https://api.unsplash.com/photos?page=1>; rel="first", """ +
                """<https://api.unsplash.com/photos?page=1>; rel="prev", """ +
                """<https://api.unsplash.com/photos?page=346>; rel="last", """ +
                """<https://api.unsplash.com/photos?page=3>; rel="next""""

    @Test
    fun `next 링크가 포함된 헤더가 주어졌을 때_다음 페이지 존재 여부를 확인하면_true 를 반환한다`() {
        // given
        val header = full

        // when
        val result = LinkHeader.hasNext(header)

        // then
        assertTrue(result)
    }

    @Test
    fun `next 링크만 있는 헤더가 주어졌을 때_다음 페이지 존재 여부를 확인하면_true 를 반환한다`() {
        // given
        val header = """<https://api.unsplash.com/photos?page=2>; rel="next""""

        // when
        val result = LinkHeader.hasNext(header)

        // then
        assertTrue(result)
    }

    @Test
    fun `prev 없이 next 가 있는 첫 페이지 헤더가 주어졌을 때_다음 페이지 존재 여부를 확인하면_true 를 반환한다`() {
        // given
        val header =
            """<https://api.unsplash.com/photos?page=1>; rel="first", """ +
                    """<https://api.unsplash.com/photos?page=346>; rel="last", """ +
                    """<https://api.unsplash.com/photos?page=2>; rel="next""""

        // when
        val result = LinkHeader.hasNext(header)

        // then
        assertTrue(result)
    }

    @Test
    fun `next 링크가 없는 마지막 페이지 헤더가 주어졌을 때_다음 페이지 존재 여부를 확인하면_false 를 반환한다`() {
        // given
        val header =
            """<https://api.unsplash.com/photos?page=1>; rel="first", """ +
                    """<https://api.unsplash.com/photos?page=345>; rel="prev", """ +
                    """<https://api.unsplash.com/photos?page=346>; rel="last""""

        // when
        val result = LinkHeader.hasNext(header)

        // then
        assertFalse(result)
    }

    @Test
    fun `null 헤더가 주어졌을 때_다음 페이지 존재 여부를 확인하면_false 를 반환한다`() {
        // given
        val header: String? = null

        // when
        val result = LinkHeader.hasNext(header)

        // then
        assertFalse(result)
    }

    @Test
    fun `빈 문자열 헤더가 주어졌을 때_다음 페이지 존재 여부를 확인하면_false 를 반환한다`() {
        // given
        val header = ""

        // when
        val result = LinkHeader.hasNext(header)

        // then
        assertFalse(result)
    }

    @Test
    fun `공백만 있는 헤더가 주어졌을 때_다음 페이지 존재 여부를 확인하면_false 를 반환한다`() {
        // given
        val header = " "

        // when
        val result = LinkHeader.hasNext(header)

        // then
        assertFalse(result)
    }

    @Test
    fun `rel 값이 next 로 시작만 하는 헤더가 주어졌을 때_다음 페이지 존재 여부를 확인하면_false 를 반환한다`() {
        // given
        val header = """<https://api.unsplash.com/photos?page=2>; rel="nextpage""""

        // when
        val result = LinkHeader.hasNext(header)

        // then
        assertFalse(result)
    }
}