package com.example.lib
import java.io.File

/**
 * LyricViewX 接口
 * 从 LyricViewX 提取，方便管理
 * @author Moriafly
 * @since 2021年1月28日16:29:16
 */
interface LyricViewXInterface {

    /**
     * Đặt màu phông chữ của lời bài hát không phải dòng hiện hành [normalColor]
     */
    fun setNormalColor(normalColor: Int)

    /**
     * Kích thước phông chữ văn bản lời bài hát bình thường [size]，单位 px
     */
    fun setNormalTextSize(size: Float)

    /**
     * Kích thước phông chữ văn bản lời bài hát hiện tại
     */
    fun setCurrentTextSize(size: Float)

    /**
     * Đặt màu phông chữ của dòng lời bài hát hiện tại
     */
    fun setCurrentColor(currentColor: Int)

    /**
     * Đặt màu phông chữ của lời bài hát đã chọn khi kéo lời bài hát
     */
    fun setTimelineTextColor(timelineTextColor: Int)

    /**
     * Đặt màu của dòng thời gian khi kéo lời bài hát
     */
    fun setTimelineColor(timelineColor: Int)

    /**
     * Đặt màu phông chữ thời gian ở bên phải khi kéo lời bài hát
     */
    fun setTimeTextColor(timeTextColor: Int)

    /**
     * Đặt văn bản được hiển thị ở giữa màn hình khi lời bài hát trống [label]，如“Không có lời bài hát”
     */
    fun setLabel(label: String)

    /**
     * Tải tệp lời bài hát
     * Dấu thời gian của lời bài hát của hai ngôn ngữ cần phải nhất quán
     * @param mainLyricFile Tệp lời bài hát bằng ngôn ngữ đầu tiên
     * @param secondLyricFile Tệp lời bài hát ngôn ngữ thứ hai, tùy chọn
     */
    fun loadLyric(mainLyricFile: File, secondLyricFile: File? = null)

    /**
     * Tải văn bản lời bài hát
     * Dấu thời gian của lời bài hát của hai ngôn ngữ cần phải nhất quán
     * @param mainLyricText Văn bản lời bài hát bằng ngôn ngữ đầu tiên
     * @param secondLyricText Văn bản lời bài hát bằng ngôn ngữ thứ hai
     */
    fun loadLyric(mainLyricText: String?, secondLyricText: String? = null)

    /**
     * Tải lời bài hát trực tuyến
     * @param lyricUrl  Địa chỉ mạng của tệp lời bài hát
     * @param charset Định dạng mã hóa
     */
    fun loadLyricByUrl(lyricUrl: String, charset: String? = "utf-8")

    /**
     * Làm mới lời bài hát
     * @param time Thời gian chơi hiện tại
     */
    fun updateTime(time: Long)

    /**
    * Đặt có cho phép kéo lời bài hát hay không
    * @param draggable thể kéo có được phép kéo không
    * @param onPlayClickListener đặt người nghe khi nhấp vào nút phát sau khi kéo lời bài hát. Nếu được phép kéo, nó không được để trống
    */
    fun setDraggable(draggable: Boolean, onPlayClickListener: OnPlayClickListener?)

    /**
     * Đặt lần nhấp
     */
    fun setOnSingerClickListener(onSingerClickListener: OnSingleClickListener?)

    /**
     * @Bổ sung mới
     * Nhận thực thể của mỗi câu trong lời bài hát hiện tại，Có thể được sử dụng để chia sẻ lời bài hát
     * @return LyricEntry Bộ sưu tập
     */
    fun getLyricEntryList(): List<LyricEntry>

    /**
     * @Bổ sung mới
     * Đặt đối tượng của từng câu trong lời bài hát hiện tại
     */
    fun setLyricEntryList(newList: List<LyricEntry>)

    /**
     * @Bổ sung mới
     * Nhận lời bài hát của dòng hiện tại
     */
    fun getCurrentLineLyricEntry(): LyricEntry?
}

/**
 * Nút phát nút nghe nhấp chuột，Nó sẽ chuyển đến vị trí phát lại được chỉ định sau khi nhấp vào
 */
interface OnPlayClickListener {
    /**
     * Khi nhấp vào nút phát, nó sẽ chuyển đến vị trí phát đã chỉ định
     * @return Sự kiện có được tiêu thụ thành công hay không, nếu tiêu thụ thành công sẽ được cập nhật UI
     */
    fun onPlayClick(time: Long): Boolean
}

/**
 * Nhấp vào Bố cục Lời bài hát
 */
interface OnSingleClickListener {
    fun onClick()
}