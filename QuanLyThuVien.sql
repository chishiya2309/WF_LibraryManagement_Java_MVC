CREATE DATABASE IF NOT EXISTS QuanLyThuVien
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE QuanLyThuVien;

CREATE TABLE DanhMucSach (
    MaDanhMuc VARCHAR(20) PRIMARY KEY,
    TenDanhMuc VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    MoTa VARCHAR(500) CHARACTER SET utf8mb4,
    DanhMucCha VARCHAR(20),
    SoLuongSach INT DEFAULT 0,
    NgayTao DATE DEFAULT (CURRENT_DATE()),
    CapNhatLanCuoi DATE DEFAULT (CURRENT_DATE()),
    TrangThai VARCHAR(20) CHARACTER SET utf8mb4 DEFAULT 'Hoạt động',
    CONSTRAINT chk_SoLuongSach CHECK (SoLuongSach >= 0),
    CONSTRAINT chk_TrangThai CHECK (TrangThai IN ('Hoạt động', 'Ngừng hoạt động')),
    CONSTRAINT fk_DanhMucCha FOREIGN KEY (DanhMucCha) REFERENCES DanhMucSach(MaDanhMuc) ON DELETE SET NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX idx_DanhMucCha ON DanhMucSach(DanhMucCha);

DELIMITER //

DELIMITER //

-- Trigger: cập nhật các danh mục con khi xóa danh mục cha
CREATE TRIGGER trg_SetNull_DanhMucSach
BEFORE DELETE ON DanhMucSach
FOR EACH ROW
BEGIN
    UPDATE DanhMucSach SET DanhMucCha = NULL WHERE DanhMucCha = OLD.MaDanhMuc;
END //

-- Trigger: cập nhật ngày sửa đổi khi thay đổi dữ liệu danh mục
CREATE TRIGGER trg_CapNhatThoiGian_DanhMucSach
BEFORE UPDATE ON DanhMucSach
FOR EACH ROW
BEGIN
    SET NEW.CapNhatLanCuoi = CURRENT_DATE();
END //

DELIMITER ;

-- tạo bảng sách
CREATE TABLE Sach (
    MaSach VARCHAR(20) PRIMARY KEY,
    ISBN VARCHAR(30) NOT NULL UNIQUE,
    TenSach VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    TacGia VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    MaDanhMuc VARCHAR(20) NOT NULL,
    NamXuatBan INT NOT NULL,
    NXB VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    SoBan INT NOT NULL,
    KhaDung INT NOT NULL,
    ViTri VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    CONSTRAINT fk_Sach_DanhMuc FOREIGN KEY (MaDanhMuc) REFERENCES DanhMucSach(MaDanhMuc) ON DELETE CASCADE,
    CONSTRAINT chk_NamXuatBan CHECK (NamXuatBan > 0),
    CONSTRAINT chk_SoBan CHECK (SoBan >= 0),
    CONSTRAINT chk_KhaDung CHECK (KhaDung >= 0),
    CONSTRAINT chk_KhaDung_SoBan CHECK (KhaDung <= SoBan)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX idx_MaDanhMuc ON Sach(MaDanhMuc);

-- tạo bảng nhân viên

CREATE TABLE NhanVien (
    ID VARCHAR(20) PRIMARY KEY,
    HoTen VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    GioiTinh VARCHAR(10) CHARACTER SET utf8mb4 NOT NULL,
    ChucVu VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    SoDienThoai VARCHAR(15) NOT NULL UNIQUE,
    NgayVaoLam DATE NOT NULL,
    TrangThai VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'Đang làm',
    CONSTRAINT chk_GioiTinh CHECK (GioiTinh IN ('Nam', 'Nữ')),
    CONSTRAINT chk_ChucVu CHECK (ChucVu IN ('Admin', 'Quản Lý', 'Nhân Viên')),
    CONSTRAINT chk_TrangThai_NhanVien CHECK (TrangThai IN ('Đang làm', 'Tạm nghỉ')),
    CONSTRAINT chk_Email CHECK (Email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX idx_Email_NhanVien ON NhanVien(Email);
CREATE INDEX idx_SoDienThoai_NhanVien ON NhanVien(SoDienThoai);

DELIMITER //

-- Kiểm tra ngày vào làm không được sau hôm nay khi thêm mới

CREATE TRIGGER trg_NhanVien_Before_Insert
BEFORE INSERT ON NhanVien
FOR EACH ROW
BEGIN
    IF NEW.NgayVaoLam > CURRENT_DATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày vào làm không được lớn hơn ngày hiện tại!';
    END IF;
END //

-- Kiểm tra ngày vào làm khi cập nhật

DELIMITER //

CREATE TRIGGER trg_NhanVien_Before_Update
BEFORE UPDATE ON NhanVien
FOR EACH ROW
BEGIN
    IF NEW.NgayVaoLam > CURRENT_DATE() THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Ngày vào làm không được lớn hơn ngày hiện tại!';
    END IF;
END //

DELIMITER ;

-- Bảng thành viên
CREATE TABLE ThanhVien (
    MaThanhVien VARCHAR(10) PRIMARY KEY,
    HoTen VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    GioiTinh VARCHAR(10) CHARACTER SET utf8mb4 NOT NULL,
    SoDienThoai VARCHAR(15) NOT NULL UNIQUE,
    Email VARCHAR(255) NOT NULL UNIQUE,
    LoaiThanhVien VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL,
    NgayDangKy DATE NOT NULL,
    NgayHetHan DATE NOT NULL,
    TrangThai VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL,
    CONSTRAINT chk_GioiTinh_ThanhVien CHECK (GioiTinh IN ('Nam', 'Nữ')),
    CONSTRAINT chk_LoaiThanhVien CHECK (LoaiThanhVien IN ('Sinh viên', 'Giảng viên', 'Thường')),
    CONSTRAINT chk_TrangThai_ThanhVien CHECK (TrangThai IN ('Hoạt động', 'Hết hạn', 'Khóa')),
    CONSTRAINT chk_Email_ThanhVien CHECK (Email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_NgayDangKy_NgayHetHan CHECK (NgayDangKy <= NgayHetHan)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Chỉ mục tìm kiếm nhanh
CREATE INDEX idx_Email_ThanhVien ON ThanhVien(Email);
CREATE INDEX idx_SoDienThoai_ThanhVien ON ThanhVien(SoDienThoai);

DELIMITER //

-- Trigger: Kiểm tra ngày đăng ký và xử lý trạng thái

CREATE TRIGGER trg_ThanhVien_Before_Insert
BEFORE INSERT ON ThanhVien
FOR EACH ROW
BEGIN
    IF NEW.NgayDangKy > CURRENT_DATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày đăng ký không được lớn hơn ngày hiện tại!';
    END IF;
    IF NEW.NgayHetHan < CURRENT_DATE() AND NEW.TrangThai = 'Hoạt động' THEN
        SET NEW.TrangThai = 'Hết hạn';
    END IF;
END //

-- Trigger: Kiểm tra lại ngày khi cập nhật
DELIMITER //

CREATE TRIGGER trg_ThanhVien_Before_Update
BEFORE UPDATE ON ThanhVien
FOR EACH ROW
BEGIN
    -- Kiểm tra ngày đăng ký không được sau ngày hiện tại
    IF NEW.NgayDangKy > CURRENT_DATE() THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Ngày đăng ký không được lớn hơn ngày hiện tại!';
    END IF;

    -- Nếu ngày hết hạn đã qua và trạng thái vẫn là Hoạt động thì đổi thành Hết hạn
    IF NEW.NgayHetHan < CURRENT_DATE() AND NEW.TrangThai = 'Hoạt động' THEN
        SET NEW.TrangThai = 'Hết hạn';
    END IF;
END //

DELIMITER ;


-- Phiếu mượn sách: quản lý lịch sử mượn và trả
CREATE TABLE PhieuMuon (
    MaPhieu INT PRIMARY KEY AUTO_INCREMENT,
    MaThanhVien VARCHAR(10) NOT NULL,
    NgayMuon DATE NOT NULL,
    HanTra DATE NOT NULL,
    NgayTraThucTe DATE,
    TrangThai VARCHAR(50) CHARACTER SET utf8mb4 DEFAULT 'Đang mượn',
    MaSach VARCHAR(20) NOT NULL,
    SoLuong INT NOT NULL,
    CONSTRAINT fk_PhieuMuon_ThanhVien FOREIGN KEY (MaThanhVien) REFERENCES ThanhVien(MaThanhVien),
    CONSTRAINT fk_PhieuMuon_Sach FOREIGN KEY (MaSach) REFERENCES Sach(MaSach),
    CONSTRAINT chk_TrangThai_PhieuMuon CHECK (TrangThai IN ('Đang mượn', 'Đã trả', 'Quá hạn')),
    CONSTRAINT chk_SoLuong CHECK (SoLuong > 0)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tăng hiệu năng truy vấn
CREATE INDEX idx_MaThanhVien ON PhieuMuon(MaThanhVien);
CREATE INDEX idx_MaSach ON PhieuMuon(MaSach);

DELIMITER //

-- Kiểm tra và cập nhật khi mượn sách
CREATE TRIGGER trg_PhieuMuon_Before_Insert
BEFORE INSERT ON PhieuMuon
FOR EACH ROW
BEGIN
    DECLARE available INT;
    IF NEW.NgayMuon > CURRENT_DATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày mượn không được lớn hơn ngày hiện tại!';
    END IF;
    IF NEW.HanTra < NEW.NgayMuon THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Hạn trả phải lớn hơn hoặc bằng ngày mượn!';
    END IF;
    SELECT KhaDung INTO available FROM Sach WHERE MaSach = NEW.MaSach;
    IF NEW.SoLuong > available THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Số lượng mượn vượt quá số sách khả dụng!';
    END IF;
    UPDATE Sach SET KhaDung = KhaDung - NEW.SoLuong WHERE MaSach = NEW.MaSach;
END //

-- Trigger cập nhật khi sửa thông tin phiếu mượn

DELIMITER //

CREATE TRIGGER trg_PhieuMuon_Before_Update
BEFORE UPDATE ON PhieuMuon
FOR EACH ROW
BEGIN
    DECLARE available INT;
    DECLARE old_quantity INT;

    IF NEW.NgayMuon > CURRENT_DATE() THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Ngày mượn không được lớn hơn ngày hiện tại!';
    END IF;

    IF NEW.HanTra < NEW.NgayMuon THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Hạn trả phải lớn hơn hoặc bằng ngày mượn!';
    END IF;

    IF NEW.SoLuong != OLD.SoLuong THEN
        SELECT KhaDung INTO available FROM Sach WHERE MaSach = NEW.MaSach;
        SET old_quantity = OLD.SoLuong;

        IF NEW.SoLuong > (available + old_quantity) THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Số lượng mượn vượt quá số khả dụng!';
        END IF;

        -- Cảnh báo: phần UPDATE này có thể gây lỗi như đã nói ở trên
        UPDATE Sach 
        SET KhaDung = KhaDung + old_quantity - NEW.SoLuong 
        WHERE MaSach = NEW.MaSach;
    END IF;
END //

DELIMITER ;

-- tạo các stored procedure

-- sp thêm sách
DELIMITER $$

CREATE PROCEDURE sp_ThemSach (
    IN p_MaSach VARCHAR(20),
    IN p_ISBN VARCHAR(30),
    IN p_TenSach VARCHAR(255),
    IN p_TacGia VARCHAR(255),
    IN p_MaDanhMuc VARCHAR(20),
    IN p_NamXuatBan INT,
    IN p_NXB VARCHAR(255),
    IN p_SoBan INT,
    IN p_ViTri VARCHAR(255)
)
BEGIN
    -- Kiểm tra trùng mã sách hoặc ISBN
    IF EXISTS (SELECT 1 FROM Sach WHERE MaSach = p_MaSach OR ISBN = p_ISBN) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Mã sách hoặc ISBN đã tồn tại!';
    END IF;

    -- Kiểm tra mã danh mục có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM DanhMucSach WHERE MaDanhMuc = p_MaDanhMuc) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Mã danh mục không tồn tại!';
    END IF;

    -- Kiểm tra năm xuất bản hợp lệ
    IF p_NamXuatBan <= 0 OR p_NamXuatBan > YEAR(CURDATE()) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Năm xuất bản không hợp lệ!';
    END IF;

    -- Kiểm tra số bản phải >= 0
    IF p_SoBan < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số bản sách không được âm!';
    END IF;

    -- Thêm sách
    INSERT INTO Sach (MaSach, ISBN, TenSach, TacGia, MaDanhMuc, NamXuatBan, NXB, SoBan, KhaDung, ViTri)
    VALUES (p_MaSach, p_ISBN, p_TenSach, p_TacGia, p_MaDanhMuc, p_NamXuatBan, p_NXB, p_SoBan, p_SoBan, p_ViTri);

    -- Cập nhật số lượng sách trong danh mục
    UPDATE DanhMucSach
    SET SoLuongSach = SoLuongSach + p_SoBan
    WHERE MaDanhMuc = p_MaDanhMuc;
END $$

DELIMITER ;

-- sp sửa thông tin sách

DELIMITER $$

CREATE PROCEDURE sp_SuaSach (
    IN p_MaSach VARCHAR(20),
    IN p_ISBN VARCHAR(30),
    IN p_TenSach VARCHAR(255),
    IN p_TacGia VARCHAR(255),
    IN p_MaDanhMuc VARCHAR(20),
    IN p_NamXuatBan INT,
    IN p_NXB VARCHAR(255),
    IN p_SoBan INT,
    IN p_KhaDung INT,
    IN p_ViTri VARCHAR(255)
)
BEGIN
    -- Kiểm tra tồn tại sách
    IF NOT EXISTS (SELECT 1 FROM Sach WHERE MaSach = p_MaSach) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Mã sách không tồn tại!';
    END IF;

    -- Kiểm tra số bản khả dụng
    IF p_KhaDung > p_SoBan THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số bản khả dụng không thể lớn hơn tổng số bản!';
    END IF;

    -- Cập nhật sách
    UPDATE Sach
    SET
        ISBN = p_ISBN,
        TenSach = p_TenSach,
        TacGia = p_TacGia,
        MaDanhMuc = p_MaDanhMuc,
        NamXuatBan = p_NamXuatBan,
        NXB = p_NXB,
        SoBan = p_SoBan,
        KhaDung = p_KhaDung,
        ViTri = p_ViTri
    WHERE MaSach = p_MaSach;
END $$

DELIMITER ;

-- sp xóa sách

DELIMITER $$

CREATE PROCEDURE sp_XoaSach (
    IN p_MaSach VARCHAR(20)
)
BEGIN
    -- Kiểm tra sách tồn tại
    IF NOT EXISTS (SELECT 1 FROM Sach WHERE MaSach = p_MaSach) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Mã sách không tồn tại!';
    END IF;

    -- Xóa sách
    DELETE FROM Sach WHERE MaSach = p_MaSach;
END $$

DELIMITER ;

-- sp thêm danh mục sách
DELIMITER $$

CREATE PROCEDURE sp_ThemDanhMucSach (
    IN p_MaDanhMuc VARCHAR(20),
    IN p_TenDanhMuc VARCHAR(255),
    IN p_MoTa VARCHAR(500),
    IN p_DanhMucCha VARCHAR(20),
    IN p_SoLuongSach INT,
    IN p_TrangThai VARCHAR(20)
)
BEGIN
    -- Mặc định giá trị nếu NULL
    SET p_MoTa = IFNULL(p_MoTa, NULL);
    SET p_DanhMucCha = IFNULL(p_DanhMucCha, NULL);
    SET p_SoLuongSach = IFNULL(p_SoLuongSach, 0);
    SET p_TrangThai = IFNULL(p_TrangThai, 'Hoạt động');

    -- Kiểm tra mã danh mục đã tồn tại
    IF EXISTS (SELECT 1 FROM DanhMucSach WHERE MaDanhMuc = p_MaDanhMuc) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Mã danh mục đã tồn tại!';
    END IF;

    -- Kiểm tra danh mục cha nếu có
    IF p_DanhMucCha IS NOT NULL AND NOT EXISTS (SELECT 1 FROM DanhMucSach WHERE MaDanhMuc = p_DanhMucCha) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Danh mục cha không tồn tại!';
    END IF;

    -- Thêm danh mục
    INSERT INTO DanhMucSach (
        MaDanhMuc, TenDanhMuc, MoTa, DanhMucCha, SoLuongSach,
        NgayTao, CapNhatLanCuoi, TrangThai
    ) VALUES (
        p_MaDanhMuc, p_TenDanhMuc, p_MoTa, p_DanhMucCha, p_SoLuongSach,
        NOW(), NOW(), p_TrangThai
    );
END $$

DELIMITER ;

-- sp sửa danh mục sách

DELIMITER $$

CREATE PROCEDURE sp_SuaDanhMucSach (
    IN p_MaDanhMuc VARCHAR(20),
    IN p_TenDanhMuc VARCHAR(255),
    IN p_MoTa VARCHAR(500),
    IN p_DanhMucCha VARCHAR(20),
    IN p_SoLuongSach INT,
    IN p_TrangThai VARCHAR(20)
)
BEGIN
    -- Kiểm tra mã danh mục có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM DanhMucSach WHERE MaDanhMuc = p_MaDanhMuc) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Mã danh mục không tồn tại!';
    END IF;

    -- Kiểm tra danh mục cha nếu có
    IF p_DanhMucCha IS NOT NULL AND NOT EXISTS (SELECT 1 FROM DanhMucSach WHERE MaDanhMuc = p_DanhMucCha) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Danh mục cha không tồn tại!';
    END IF;

    -- Cập nhật
    UPDATE DanhMucSach
    SET 
        TenDanhMuc = COALESCE(p_TenDanhMuc, TenDanhMuc),
        MoTa = COALESCE(p_MoTa, MoTa),
        DanhMucCha = COALESCE(p_DanhMucCha, DanhMucCha),
        SoLuongSach = COALESCE(p_SoLuongSach, SoLuongSach),
        CapNhatLanCuoi = NOW(),
        TrangThai = COALESCE(p_TrangThai, TrangThai)
    WHERE MaDanhMuc = p_MaDanhMuc;
END $$

DELIMITER ;

-- sp xóa danh mục sách
DELIMITER $$

CREATE PROCEDURE sp_XoaDanhMucSach (
    IN p_MaDanhMuc VARCHAR(20)
)
BEGIN
    -- Kiểm tra mã danh mục có tồn tại
    IF NOT EXISTS (SELECT 1 FROM DanhMucSach WHERE MaDanhMuc = p_MaDanhMuc) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Mã danh mục không tồn tại!';
    END IF;

    -- Kiểm tra có danh mục con không
    IF EXISTS (SELECT 1 FROM DanhMucSach WHERE DanhMucCha = p_MaDanhMuc) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Không thể xóa danh mục vì có danh mục con!';
    END IF;

    -- Xóa danh mục
    DELETE FROM DanhMucSach WHERE MaDanhMuc = p_MaDanhMuc;
END $$

DELIMITER ;

-- sp thêm thành viên
DELIMITER $$

CREATE PROCEDURE sp_ThemThanhVien (
    IN p_MaThanhVien VARCHAR(10),
    IN p_HoTen VARCHAR(100),  
    IN p_GioiTinh VARCHAR(10),  
    IN p_SoDienThoai VARCHAR(15),
    IN p_Email VARCHAR(255), 
    IN p_LoaiThanhVien VARCHAR(50),  
    IN p_NgayDangKy DATE,
    IN p_NgayHetHan DATE,
    IN p_TrangThai VARCHAR(20) 
)
BEGIN
    DECLARE v_NgayDangKy DATE;
    DECLARE v_NgayHetHan DATE;

    -- Ngày đăng ký mặc định là ngày hiện tại nếu NULL
    SET v_NgayDangKy = IFNULL(p_NgayDangKy, CURDATE());

    -- Ngày hết hạn mặc định là 2 năm sau
    SET v_NgayHetHan = IFNULL(p_NgayHetHan, DATE_ADD(v_NgayDangKy, INTERVAL 2 YEAR));

    -- Kiểm tra mã thành viên
    IF EXISTS (SELECT 1 FROM ThanhVien WHERE MaThanhVien = p_MaThanhVien) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Mã thành viên đã tồn tại!';
    END IF;

    -- Kiểm tra số điện thoại
    IF EXISTS (SELECT 1 FROM ThanhVien WHERE SoDienThoai = p_SoDienThoai) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Số điện thoại đã tồn tại trong hệ thống!';
    END IF;

    -- Kiểm tra email
    IF EXISTS (SELECT 1 FROM ThanhVien WHERE Email = p_Email) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email đã tồn tại trong hệ thống!';
    END IF;

    -- Kiểm tra giới tính
    IF p_GioiTinh NOT IN ('Nam', 'Nữ') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Giới tính phải là Nam hoặc Nữ!';
    END IF;

    -- Kiểm tra loại thành viên
    IF p_LoaiThanhVien NOT IN ('Sinh viên', 'Giảng viên', 'Thường') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Loại thành viên không hợp lệ!';
    END IF;

    -- Kiểm tra trạng thái
    IF p_TrangThai NOT IN ('Hoạt động', 'Hết hạn', 'Khóa') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Trạng thái không hợp lệ!';
    END IF;

    -- Thêm dữ liệu
    INSERT INTO ThanhVien (MaThanhVien, HoTen, GioiTinh, SoDienThoai, Email, LoaiThanhVien, NgayDangKy, NgayHetHan, TrangThai)
    VALUES (p_MaThanhVien, p_HoTen, p_GioiTinh, p_SoDienThoai, p_Email, p_LoaiThanhVien, v_NgayDangKy, v_NgayHetHan, p_TrangThai);
END$$

DELIMITER ;

-- sp sửa thành viên
DELIMITER //

CREATE PROCEDURE sp_SuaThanhVien (
    IN p_MaThanhVien VARCHAR(10),
    IN p_HoTen VARCHAR(100),
    IN p_GioiTinh VARCHAR(10),
    IN p_SoDienThoai VARCHAR(15),
    IN p_Email VARCHAR(255),
    IN p_LoaiThanhVien VARCHAR(50),
    IN p_NgayDangKy DATE,
    IN p_NgayHetHan DATE,
    IN p_TrangThai VARCHAR(20)
)
BEGIN
    -- ⚠️ Đặt các khai báo ở đầu BEGIN
    DECLARE v_Count INT;

    -- Xử lý lỗi và rollback nếu có
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Lỗi khi cập nhật thông tin thành viên. Vui lòng kiểm tra lại dữ liệu!';
    END;

    -- Bắt đầu giao dịch
    START TRANSACTION;

    -- Kiểm tra mã thành viên
    SELECT COUNT(*) INTO v_Count FROM ThanhVien WHERE MaThanhVien = p_MaThanhVien;
    IF v_Count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Mã thành viên không tồn tại!';
    END IF;

    -- Kiểm tra số điện thoại trùng (trừ chính mình)
    IF p_SoDienThoai IS NOT NULL AND p_SoDienThoai != '' THEN
        SELECT COUNT(*) INTO v_Count FROM ThanhVien WHERE SoDienThoai = p_SoDienThoai AND MaThanhVien != p_MaThanhVien;
        IF v_Count > 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Số điện thoại đã tồn tại trong hệ thống!';
        END IF;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Số điện thoại không được để trống!';
    END IF;

    -- Kiểm tra email trùng và định dạng (trừ chính mình)
    IF p_Email IS NOT NULL AND p_Email != '' THEN
        IF p_Email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Định dạng email không hợp lệ!';
        END IF;
        SELECT COUNT(*) INTO v_Count FROM ThanhVien WHERE Email = p_Email AND MaThanhVien != p_MaThanhVien;
        IF v_Count > 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email đã tồn tại trong hệ thống!';
        END IF;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email không được để trống!';
    END IF;

    -- Kiểm tra hợp lệ các trường nếu có giá trị
    IF p_HoTen IS NULL OR p_HoTen = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Họ tên không được để trống!';
    END IF;

    IF p_GioiTinh IS NOT NULL AND p_GioiTinh NOT IN ('Nam', 'Nữ') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Giới tính không hợp lệ!';
    END IF;

    IF p_LoaiThanhVien IS NOT NULL AND p_LoaiThanhVien NOT IN ('Sinh viên', 'Giảng viên', 'Thường') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Loại thành viên không hợp lệ!';
    END IF;

    IF p_TrangThai IS NOT NULL AND p_TrangThai NOT IN ('Hoạt động', 'Hết hạn', 'Khóa') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Trạng thái không hợp lệ!';
    END IF;

    -- Kiểm tra NgayDangKy và NgayHetHan
    IF p_NgayDangKy IS NOT NULL AND p_NgayDangKy > CURRENT_DATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày đăng ký không được lớn hơn ngày hiện tại!';
    END IF;

    IF p_NgayDangKy IS NOT NULL AND p_NgayHetHan IS NOT NULL AND p_NgayDangKy > p_NgayHetHan THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày đăng ký phải nhỏ hơn hoặc bằng ngày hết hạn!';
    END IF;

    IF p_NgayHetHan IS NOT NULL AND p_NgayHetHan < CURRENT_DATE() AND p_TrangThai = 'Hoạt động' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày hết hạn trong quá khứ, trạng thái sẽ được đặt thành Hết hạn bởi trigger!';
    END IF;

    -- Cập nhật thông tin
    UPDATE ThanhVien
    SET HoTen = COALESCE(p_HoTen, HoTen),
        GioiTinh = COALESCE(p_GioiTinh, GioiTinh),
        SoDienThoai = COALESCE(p_SoDienThoai, SoDienThoai),
        Email = COALESCE(p_Email, Email),
        LoaiThanhVien = COALESCE(p_LoaiThanhVien, LoaiThanhVien),
        NgayDangKy = COALESCE(p_NgayDangKy, NgayDangKy),
        NgayHetHan = COALESCE(p_NgayHetHan, NgayHetHan),
        TrangThai = COALESCE(p_TrangThai, TrangThai)
    WHERE MaThanhVien = p_MaThanhVien;

    -- Kết thúc giao dịch
    COMMIT;
END //

DELIMITER ;

-- sp xóa thành viên
DELIMITER $$

CREATE PROCEDURE sp_XoaThanhVien (
    IN p_MaThanhVien VARCHAR(10)
)
BEGIN
    DECLARE v_Count INT;

    -- Kiểm tra mã tồn tại
    SELECT COUNT(*) INTO v_Count FROM ThanhVien WHERE MaThanhVien = p_MaThanhVien;
    IF v_Count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Mã thành viên không tồn tại!';
    END IF;

    -- Kiểm tra có đang mượn sách không
    SELECT COUNT(*) INTO v_Count FROM PhieuMuon WHERE MaThanhVien = p_MaThanhVien AND TrangThai = 'Đang mượn';
    IF v_Count > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Không thể xóa vì thành viên đang mượn sách!';
    END IF;

    -- Xóa
    DELETE FROM ThanhVien WHERE MaThanhVien = p_MaThanhVien;
END$$

DELIMITER ;

-- sp thêm nhân viên
DELIMITER $$

CREATE PROCEDURE sp_ThemNhanVien (
    IN p_ID VARCHAR(20),
    IN p_HoTen VARCHAR(100),
    IN p_GioiTinh VARCHAR(10),
    IN p_ChucVu VARCHAR(50),
    IN p_Email VARCHAR(255),
    IN p_SoDienThoai VARCHAR(15),
    IN p_NgayVaoLam DATE,
    IN p_TrangThai VARCHAR(20)
)
BEGIN
    DECLARE v_Today DATE;

    SET v_Today = CURDATE();
    SET p_NgayVaoLam = IFNULL(p_NgayVaoLam, v_Today);
    SET p_TrangThai = IFNULL(p_TrangThai, 'Đang làm');

    -- Kiểm tra ID đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM NhanVien WHERE ID = p_ID) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID nhân viên đã tồn tại!';
    END IF;

    -- Kiểm tra số điện thoại đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM NhanVien WHERE SoDienThoai = p_SoDienThoai) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Số điện thoại đã tồn tại trong hệ thống!';
    END IF;

    -- Kiểm tra email đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM NhanVien WHERE Email = p_Email) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email đã tồn tại trong hệ thống!';
    END IF;

    -- Kiểm tra giới tính hợp lệ
    IF p_GioiTinh NOT IN ('Nam', 'Nữ') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Giới tính phải là Nam hoặc Nữ!';
    END IF;

    -- Kiểm tra chức vụ hợp lệ
    IF p_ChucVu NOT IN ('Admin', 'Quản Lý', 'Nhân Viên') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Chức vụ phải là Admin, Quản Lý hoặc Nhân Viên!';
    END IF;

    -- Kiểm tra trạng thái hợp lệ
    IF p_TrangThai NOT IN ('Đang làm', 'Tạm nghỉ') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Trạng thái phải là Đang làm hoặc Tạm nghỉ!';
    END IF;

    -- Thêm nhân viên
    INSERT INTO NhanVien (ID, HoTen, GioiTinh, ChucVu, Email, SoDienThoai, NgayVaoLam, TrangThai)
    VALUES (p_ID, p_HoTen, p_GioiTinh, p_ChucVu, p_Email, p_SoDienThoai, p_NgayVaoLam, p_TrangThai);
END$$

DELIMITER ;

-- sp sửa nhân viên

DELIMITER $$

CREATE PROCEDURE sp_SuaNhanVien (
    IN p_ID VARCHAR(20),
    IN p_HoTen VARCHAR(100),
    IN p_GioiTinh VARCHAR(10),
    IN p_ChucVu VARCHAR(50),
    IN p_Email VARCHAR(255),
    IN p_SoDienThoai VARCHAR(15),
    IN p_NgayVaoLam DATE,
    IN p_TrangThai VARCHAR(20)
)
BEGIN
    DECLARE v_HoTen VARCHAR(100);
    DECLARE v_GioiTinh VARCHAR(10);
    DECLARE v_ChucVu VARCHAR(50);
    DECLARE v_Email VARCHAR(255);
    DECLARE v_SoDienThoai VARCHAR(15);
    DECLARE v_NgayVaoLam DATE;
    DECLARE v_TrangThai VARCHAR(20);

    -- Kiểm tra nhân viên tồn tại
    IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE ID = p_ID) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID nhân viên không tồn tại!';
    END IF;

    -- Lấy thông tin hiện tại
    SELECT HoTen, GioiTinh, ChucVu, Email, SoDienThoai, NgayVaoLam, TrangThai
    INTO v_HoTen, v_GioiTinh, v_ChucVu, v_Email, v_SoDienThoai, v_NgayVaoLam, v_TrangThai
    FROM NhanVien WHERE ID = p_ID;

    -- Kiểm tra trùng số điện thoại
    IF p_SoDienThoai IS NOT NULL AND p_SoDienThoai <> v_SoDienThoai AND 
       EXISTS (SELECT 1 FROM NhanVien WHERE SoDienThoai = p_SoDienThoai AND ID <> p_ID) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Số điện thoại đã tồn tại trong hệ thống!';
    END IF;

    -- Kiểm tra trùng email
    IF p_Email IS NOT NULL AND p_Email <> v_Email AND 
       EXISTS (SELECT 1 FROM NhanVien WHERE Email = p_Email AND ID <> p_ID) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email đã tồn tại trong hệ thống!';
    END IF;

    -- Kiểm tra giới tính hợp lệ
    IF p_GioiTinh IS NOT NULL AND p_GioiTinh NOT IN ('Nam', 'Nữ') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Giới tính phải là Nam hoặc Nữ!';
    END IF;

    -- Kiểm tra chức vụ hợp lệ
    IF p_ChucVu IS NOT NULL AND p_ChucVu NOT IN ('Admin', 'Quản Lý', 'Nhân Viên') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Chức vụ phải là Admin, Quản Lý hoặc Nhân Viên!';
    END IF;

    -- Kiểm tra trạng thái hợp lệ
    IF p_TrangThai IS NOT NULL AND p_TrangThai NOT IN ('Đang làm', 'Tạm nghỉ') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Trạng thái phải là Đang làm hoặc Tạm nghỉ!';
    END IF;

    -- Thực hiện cập nhật
    UPDATE NhanVien
    SET HoTen = COALESCE(p_HoTen, v_HoTen),
        GioiTinh = COALESCE(p_GioiTinh, v_GioiTinh),
        ChucVu = COALESCE(p_ChucVu, v_ChucVu),
        Email = COALESCE(p_Email, v_Email),
        SoDienThoai = COALESCE(p_SoDienThoai, v_SoDienThoai),
        NgayVaoLam = COALESCE(p_NgayVaoLam, v_NgayVaoLam),
        TrangThai = COALESCE(p_TrangThai, v_TrangThai)
    WHERE ID = p_ID;
END $$

DELIMITER ;

-- sp xóa nhân viên

DELIMITER $$

CREATE PROCEDURE sp_XoaNhanVien (
    IN p_ID VARCHAR(20)
)
BEGIN
    -- Kiểm tra nhân viên tồn tại
    IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE ID = p_ID) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Không tìm thấy nhân viên để xóa!';
    END IF;

    -- Xóa nhân viên
    DELETE FROM NhanVien WHERE ID = p_ID;
END $$

DELIMITER ;

-- sp để tạo phiếu mượn
DELIMITER //

CREATE PROCEDURE sp_MuonSach(
    IN p_MaThanhVien VARCHAR(10),
    IN p_MaSach VARCHAR(20),
    IN p_SoLuong INT,
    IN p_NgayMuon DATE,
    IN p_HanTra DATE
)
BEGIN
    DECLARE v_SoLuongKhaDung INT;
    DECLARE v_SoSachDangMuon INT;
    DECLARE v_Msg TEXT;

    -- Xử lý lỗi và rollback nếu có
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Lỗi khi tạo phiếu mượn. Vui lòng kiểm tra lại dữ liệu!';
    END;

    START TRANSACTION;

    -- Kiểm tra đầu vào cơ bản
    IF p_MaThanhVien IS NULL OR p_MaThanhVien = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Mã thành viên không được để trống!';
    END IF;

    IF p_MaSach IS NULL OR p_MaSach = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Mã sách không được để trống!';
    END IF;

    SET p_NgayMuon = IFNULL(p_NgayMuon, CURDATE());
    SET p_HanTra = IFNULL(p_HanTra, DATE_ADD(p_NgayMuon, INTERVAL 14 DAY));

    IF p_SoLuong <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Số lượng mượn phải lớn hơn 0!';
    END IF;

    -- Kiểm tra thành viên
    IF NOT EXISTS (SELECT 1 FROM ThanhVien WHERE MaThanhVien = p_MaThanhVien) THEN
        SET v_Msg = CONCAT('Mã thành viên ', p_MaThanhVien, ' không tồn tại!');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_Msg;
    END IF;

    -- Kiểm tra sách
    IF NOT EXISTS (SELECT 1 FROM Sach WHERE MaSach = p_MaSach) THEN
        SET v_Msg = CONCAT('Mã sách ', p_MaSach, ' không tồn tại!');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_Msg;
    END IF;

    -- Kiểm tra trạng thái thành viên
    IF EXISTS (
        SELECT 1 FROM ThanhVien 
        WHERE MaThanhVien = p_MaThanhVien AND TrangThai IN ('Khóa', 'Hết hạn')
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Thành viên đã bị khóa hoặc tài khoản đã hết hạn!';
    END IF;

    -- Kiểm tra số sách khả dụng
    SELECT KhaDung INTO v_SoLuongKhaDung FROM Sach WHERE MaSach = p_MaSach;

    IF v_SoLuongKhaDung < p_SoLuong THEN
        SET v_Msg = CONCAT('Số lượng sách không đủ để mượn! Hiện có ', v_SoLuongKhaDung, ' cuốn khả dụng.');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_Msg;
    END IF;

    -- Kiểm tra số lượng sách đang mượn
    SELECT IFNULL(SUM(SoLuong), 0) INTO v_SoSachDangMuon
    FROM PhieuMuon
    WHERE MaThanhVien = p_MaThanhVien AND TrangThai = 'Đang mượn';

    IF (v_SoSachDangMuon + p_SoLuong) > 5 THEN
        SET v_Msg = CONCAT('Thành viên đã mượn ', v_SoSachDangMuon, ' cuốn, không thể mượn thêm ', p_SoLuong, ' cuốn nữa! Tối đa là 5 cuốn.');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_Msg;
    END IF;

    -- Kiểm tra có phiếu quá hạn không
    IF EXISTS (
        SELECT 1 FROM PhieuMuon 
        WHERE MaThanhVien = p_MaThanhVien AND TrangThai = 'Quá hạn'
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Thành viên có sách mượn quá hạn chưa trả, không thể mượn thêm!';
    END IF;

    -- Kiểm tra ngày mượn - hạn trả
    IF p_NgayMuon > CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày mượn không được lớn hơn ngày hiện tại!';
    END IF;

    IF p_HanTra < p_NgayMuon THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Hạn trả phải lớn hơn hoặc bằng ngày mượn!';
    END IF;

    IF p_HanTra < CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Hạn trả trong quá khứ, phiếu mượn sẽ được đặt trạng thái Quá hạn bởi trigger!';
    END IF;

    -- Tạo phiếu mượn
    INSERT INTO PhieuMuon (MaThanhVien, NgayMuon, HanTra, TrangThai, MaSach, SoLuong)
    VALUES (p_MaThanhVien, p_NgayMuon, p_HanTra, 'Đang mượn', p_MaSach, p_SoLuong);

    -- Cập nhật lại sách
    UPDATE Sach
    SET KhaDung = KhaDung - p_SoLuong
    WHERE MaSach = p_MaSach;

    COMMIT;

    SELECT 'Tạo phiếu mượn thành công!' AS Message;
END //

DELIMITER ;

-- sp trả sách
DELIMITER //

CREATE PROCEDURE sp_TraSach(
    IN p_MaPhieu INT,
    IN p_NgayTra DATE
)
BEGIN
    DECLARE v_MaSach VARCHAR(20);
    DECLARE v_SoLuong INT;
    DECLARE v_HanTra DATE;
    DECLARE v_NgayMuon DATE;
    DECLARE v_TrangThai VARCHAR(50);

    -- Bắt đầu giao dịch
    START TRANSACTION;

    -- Kiểm tra phiếu mượn có tồn tại và trạng thái hợp lệ
    IF NOT EXISTS (SELECT 1 FROM PhieuMuon WHERE MaPhieu = p_MaPhieu AND (TrangThai = 'Đang mượn' OR TrangThai = 'Quá hạn')) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Phiếu mượn không tồn tại hoặc không ở trạng thái cho phép trả sách!';
    END IF;

    -- Lấy thông tin phiếu mượn
    SELECT MaSach, SoLuong, HanTra, NgayMuon, TrangThai
    INTO v_MaSach, v_SoLuong, v_HanTra, v_NgayMuon, v_TrangThai
    FROM PhieuMuon
    WHERE MaPhieu = p_MaPhieu;

    -- Kiểm tra ngày trả phải lớn hơn ngày mượn
    IF p_NgayTra <= v_NgayMuon THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày trả thực tế phải lớn hơn ngày mượn!';
    END IF;

    -- Cập nhật trạng thái phiếu mượn thành 'Đã trả' và ghi nhận ngày trả thực tế
    UPDATE PhieuMuon
    SET TrangThai = 'Đã trả', NgayTraThucTe = p_NgayTra
    WHERE MaPhieu = p_MaPhieu;

    -- Cập nhật số lượng sách khả dụng
    UPDATE Sach
    SET KhaDung = KhaDung + v_SoLuong
    WHERE MaSach = v_MaSach;

    -- Commit giao dịch
    COMMIT;

    -- Trả kết quả thành công
    SELECT 'Trả sách thành công!' AS Message;
END //

DELIMITER ;

-- sp sửa phiếu mượn
DELIMITER //

CREATE PROCEDURE sp_SuaPhieuMuon(
    IN p_MaPhieu INT,
    IN p_NgayMuon DATE,
    IN p_HanTra DATE,
    IN p_NgayTraThucTe DATE,
    IN p_TrangThai VARCHAR(50),
    IN p_SoLuong INT
)
BEGIN
    -- Declare variables
    DECLARE v_MaSach VARCHAR(20);
    DECLARE v_SoLuongCu INT;
    DECLARE v_TrangThaiCu VARCHAR(50);
    DECLARE v_MaThanhVien VARCHAR(10);
    DECLARE v_TongSachDangMuon INT;
    DECLARE v_KhaDung INT;
    DECLARE v_Msg TEXT;

    -- Error handler
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Lỗi khi sửa phiếu mượn. Vui lòng kiểm tra lại dữ liệu!';
    END;

    -- Bắt đầu giao dịch
    START TRANSACTION;

    -- Kiểm tra phiếu mượn có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM PhieuMuon WHERE MaPhieu = p_MaPhieu) THEN
        SET v_Msg = CONCAT('Phiếu mượn ', p_MaPhieu, ' không tồn tại!');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_Msg;
    END IF;

    -- Lấy thông tin phiếu mượn hiện tại
    SELECT MaSach, SoLuong, TrangThai, MaThanhVien
    INTO v_MaSach, v_SoLuongCu, v_TrangThaiCu, v_MaThanhVien
    FROM PhieuMuon
    WHERE MaPhieu = p_MaPhieu;

    -- Kiểm tra số lượng hợp lệ
    IF p_SoLuong <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Số lượng mượn phải lớn hơn 0!';
    END IF;

    -- Kiểm tra số lượng sách khả dụng nếu tăng số lượng
    IF p_SoLuong > v_SoLuongCu THEN
        SELECT KhaDung INTO v_KhaDung FROM Sach WHERE MaSach = v_MaSach;
        IF v_KhaDung < (p_SoLuong - v_SoLuongCu) THEN
            SET v_Msg = CONCAT('Số lượng sách không đủ! Hiện có ', v_KhaDung, ' cuốn khả dụng.');
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_Msg;
        END IF;
    END IF;

    -- Kiểm tra ngày mượn
    IF p_NgayMuon > CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày mượn không được lớn hơn ngày hiện tại!';
    END IF;

    -- Kiểm tra ngày trả thực tế
    IF p_NgayTraThucTe IS NOT NULL AND p_NgayTraThucTe < p_NgayMuon THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày trả thực tế phải lớn hơn hoặc bằng ngày mượn!';
    END IF;

    -- Kiểm tra hạn trả
    IF p_HanTra < p_NgayMuon THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Hạn trả phải lớn hơn hoặc bằng ngày mượn!';
    END IF;

    -- Kiểm tra trạng thái hợp lệ
    IF p_TrangThai NOT IN ('Đang mượn', 'Đã trả', 'Quá hạn') THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Trạng thái không hợp lệ! Chỉ chấp nhận: Đang mượn, Đã trả, Quá hạn.';
    END IF;

    -- Kiểm tra giới hạn số lượng sách mượn
    IF p_TrangThai IN ('Đang mượn', 'Quá hạn') THEN
        SELECT IFNULL(SUM(SoLuong), 0) INTO v_TongSachDangMuon
        FROM PhieuMuon
        WHERE MaThanhVien = v_MaThanhVien
            AND MaPhieu != p_MaPhieu
            AND TrangThai IN ('Đang mượn', 'Quá hạn');

        IF (v_TongSachDangMuon + p_SoLuong) > 5 THEN
            SET v_Msg = CONCAT('Thành viên đã mượn ', v_TongSachDangMuon, 
                               ' cuốn, không thể mượn thêm ', p_SoLuong, 
                               ' cuốn nữa! Tối đa là 5 cuốn.');
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_Msg;
        END IF;
    END IF;

    -- Cập nhật phiếu mượn
    UPDATE PhieuMuon
    SET NgayMuon = p_NgayMuon,
        HanTra = p_HanTra,
        NgayTraThucTe = p_NgayTraThucTe,
        TrangThai = p_TrangThai,
        SoLuong = p_SoLuong
    WHERE MaPhieu = p_MaPhieu;

    -- Commit giao dịch
    COMMIT;

    -- Trả kết quả thành công
    SELECT 'Sửa phiếu mượn thành công!' AS Message;

END //

DELIMITER ;

-- sp xóa phiếu mượn
DELIMITER $$

CREATE PROCEDURE sp_XoaPhieuMuon(
    IN p_MaPhieu INT
)
BEGIN
    DECLARE v_TrangThai VARCHAR(50);
    DECLARE v_MaSach VARCHAR(20);
    DECLARE v_SoLuong INT;

    -- Kiểm tra phiếu mượn có tồn tại không
    IF NOT EXISTS (SELECT 1 FROM PhieuMuon WHERE MaPhieu = p_MaPhieu) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Mã phiếu mượn không tồn tại!';
    END IF;

    -- Lấy thông tin của phiếu mượn
    SELECT TrangThai, MaSach, SoLuong
    INTO v_TrangThai, v_MaSach, v_SoLuong
    FROM PhieuMuon
    WHERE MaPhieu = p_MaPhieu;

    -- Bắt đầu giao dịch
    START TRANSACTION;

    -- Nếu phiếu đang ở trạng thái "Đang mượn" hoặc "Quá hạn", cập nhật lại số lượng sách khả dụng
    IF v_TrangThai IN ('Đang mượn', 'Quá hạn') THEN
        UPDATE Sach
        SET KhaDung = KhaDung + v_SoLuong
        WHERE MaSach = v_MaSach;
    END IF;

    -- Xóa phiếu mượn
    DELETE FROM PhieuMuon WHERE MaPhieu = p_MaPhieu;

    -- Commit giao dịch
    COMMIT;

    -- Trả kết quả thành công
    SELECT 'Xóa phiếu mượn thành công!' AS Message;
END $$

DELIMITER ;

-- sp thống kê tổng quan
DELIMITER $$

CREATE PROCEDURE sp_ThongKeTongQuan()
BEGIN
    -- Thống kê sách
    SELECT 
        COUNT(*) AS TongSoSach,
        SUM(KhaDung) AS TongSachKhaDung
    FROM Sach;

    -- Thống kê thành viên
    SELECT 
        COUNT(*) AS TongThanhVien
    FROM ThanhVien 
    WHERE TrangThai = 'Hoạt động';

    -- Thống kê nhân viên
    SELECT 
        COUNT(*) AS TongNhanVien
    FROM NhanVien 
    WHERE TrangThai = 'Đang làm';

    -- Thống kê phiếu mượn
    SELECT 
        -- Sách mượn hôm nay
        (SELECT COUNT(*) FROM PhieuMuon WHERE DATE(NgayMuon) = CURDATE()) AS SachMuonHomNay,
        
        -- Sách trả hôm nay
        (SELECT COUNT(*) FROM PhieuMuon WHERE DATE(NgayTraThucTe) = CURDATE()) AS SachTraHomNay,
        
        -- Sách đang quá hạn
        (SELECT COUNT(*) FROM PhieuMuon WHERE TrangThai = 'Quá hạn') AS SachQuaHan,
        
        -- Sách trả trễ (đã trả nhưng sau hạn trả)
        (SELECT COUNT(*) FROM PhieuMuon 
         WHERE TrangThai = 'Đã trả' 
           AND NgayTraThucTe > HanTra) AS SachTraTre;
END $$

DELIMITER ;

-- Index cho bảng Sach - tăng tốc truy vấn tổng số sách khả dụng
CREATE INDEX IX_Sach_KhaDung ON Sach(KhaDung);

-- Index cho bảng PhieuMuon theo NgayMuon - tăng tốc truy vấn sách mượn theo ngày
CREATE INDEX IX_PhieuMuon_NgayMuon ON PhieuMuon(NgayMuon);

-- Index cho bảng PhieuMuon theo NgayTraThucTe - tăng tốc truy vấn sách trả theo ngày
CREATE INDEX IX_PhieuMuon_NgayTraThucTe ON PhieuMuon(NgayTraThucTe);

-- Index cho bảng PhieuMuon theo TrangThai - tăng tốc truy vấn sách quá hạn
CREATE INDEX IX_PhieuMuon_TrangThai ON PhieuMuon(TrangThai);

-- Index tổng hợp cho truy vấn sách trả hôm nay
CREATE INDEX IX_PhieuMuon_TrangThai_NgayTra ON PhieuMuon(TrangThai, NgayTraThucTe);

-- Thêm dữ liệu danh mục sách
INSERT INTO DanhMucSach (MaDanhMuc, TenDanhMuc, MoTa, DanhMucCha, SoLuongSach, NgayTao, CapNhatLanCuoi, TrangThai) VALUES 
('CAT001', 'Văn học', 'Các tác phẩm văn học', NULL, 2100, '2023-01-01', '2024-03-05', 'Hoạt động'),
('CAT002', 'Giáo dục', 'Sách giáo dục và học tập', NULL, 1250, '2023-01-01', '2024-03-05', 'Hoạt động'),
('CAT003', 'Kinh tế & Kinh doanh', 'Sách về kinh tế và kinh doanh', NULL, 680, '2023-01-01', '2024-03-05', 'Hoạt động'),
('CAT004', 'Khoa học & Công nghệ', 'Sách khoa học và công nghệ', NULL, 780, '2023-01-01', '2024-03-05', 'Hoạt động'),
('CAT005', 'Phát triển bản thân', 'Sách phát triển kỹ năng cá nhân', NULL, 430, '2023-01-01', '2024-03-05', 'Hoạt động'),
('CAT101', 'Tiểu thuyết', 'Các tác phẩm văn học dài', 'CAT001', 1250, '2023-01-05', '2024-03-10', 'Hoạt động'),
('CAT102', 'Truyện ngắn', 'Các truyện ngắn và tuyển tập', 'CAT001', 430, '2023-01-05', '2024-03-10', 'Hoạt động'),
('CAT103', 'Thơ ca', 'Các tác phẩm thơ', 'CAT001', 210, '2023-01-05', '2024-03-10', 'Hoạt động'),
('CAT104', 'Truyện thiếu nhi', 'Sách dành cho thiếu nhi', 'CAT001', 210, '2023-01-05', '2024-03-10', 'Hoạt động'),
('CAT201', 'Sách giáo khoa', 'Sách học tập các cấp', 'CAT002', 850, '2023-01-08', '2024-03-15', 'Hoạt động'),
('CAT202', 'Sách tham khảo', 'Sách bổ trợ kiến thức', 'CAT002', 320, '2023-01-08', '2024-03-15', 'Hoạt động'),
('CAT203', 'Từ điển & Bách khoa', 'Sách tra cứu', 'CAT002', 80, '2023-01-08', '2024-03-15', 'Hoạt động'),
('CAT301', 'Quản trị kinh doanh', 'Sách về quản lý và điều hành', 'CAT003', 280, '2023-01-10', '2024-03-18', 'Hoạt động'),
('CAT302', 'Marketing & Bán hàng', 'Sách về tiếp thị và bán hàng', 'CAT003', 170, '2023-01-10', '2024-03-18', 'Hoạt động'),
('CAT303', 'Tài chính & Đầu tư', 'Sách về tài chính cá nhân', 'CAT003', 230, '2023-01-10', '2024-03-18', 'Hoạt động'),
('CAT401', 'Công nghệ thông tin', 'Sách về CNTT và lập trình', 'CAT004', 310, '2023-01-15', '2024-03-20', 'Hoạt động');

-- Thêm dữ liệu sách
INSERT INTO Sach (MaSach, ISBN, TenSach, TacGia, MaDanhMuc, NamXuatBan, NXB, SoBan, KhaDung, ViTri) VALUES
('B1001', '9780747532743', 'Harry Potter và Hòn đá Phù thủy', 'J.K. Rowling', 'CAT101', 1997, 'Nhà xuất bản Trẻ', 10, 8, 'A-12-3'),
('B1002', '9780747538486', 'Harry Potter và Phòng chứa Bí mật', 'J.K. Rowling', 'CAT101', 1998, 'Nhà xuất bản Trẻ', 8, 5, 'A-12-4'),
('B1003', '9780747542155', 'Harry Potter và Tên tù nhân ngục Azkaban', 'J.K. Rowling', 'CAT101', 1999, 'Nhà xuất bản Trẻ', 7, 4, 'A-12-5'),
('B1004', '9780439139595', 'Harry Potter và Chiếc cốc lửa', 'J.K. Rowling', 'CAT101', 2000, 'Nhà xuất bản Trẻ', 10, 7, 'A-12-6'),
('B1005', '9780439358064', 'Harry Potter và Hội Phượng Hoàng', 'J.K. Rowling', 'CAT101', 2003, 'Nhà xuất bản Trẻ', 12, 9, 'A-12-7'),
('B1006', '9780439785969', 'Harry Potter và Hoàng tử lai', 'J.K. Rowling', 'CAT101', 2005, 'Nhà xuất bản Trẻ', 15, 11, 'A-12-8'),
('B1007', '9780545139700', 'Harry Potter và Bảo bối Tử thần', 'J.K. Rowling', 'CAT101', 2007, 'Nhà xuất bản Trẻ', 20, 15, 'A-12-9'),
('B1008', '9780590353427', 'Chú bé phù thủy', 'Roald Dahl', 'CAT104', 1983, 'Nhà xuất bản Kim Đồng', 5, 3, 'B-03-2'),
('B1009', '9780747546290', 'Matilda', 'Roald Dahl', 'CAT104', 1988, 'Nhà xuất bản Kim Đồng', 7, 5, 'B-03-3'),
('B1010', '9780140328721', 'Bí kíp làm giàu', 'Napoleon Hill', 'CAT303', 1937, 'Nhà xuất bản Lao động', 3, 2, 'C-05-1'),
('B1011', '9780062457714', 'Sức mạnh của thói quen', 'Charles Duhigg', 'CAT203', 2012, 'Nhà xuất bản Lao động', 4, 2, 'C-07-2'),
('B1012', '9786048412234', 'Đắc nhân tâm', 'Dale Carnegie', 'CAT005', 1936, 'Nhà xuất bản Tổng hợp TPHCM', 10, 8, 'D-01-1'),
('B1013', '9786045512838', 'Nhà giả kim', 'Paulo Coelho', 'CAT101', 1988, 'Nhà xuất bản Văn học', 15, 12, 'A-05-2'),
('B1014', '9780671027032', '7 Thói quen của người thành đạt', 'Stephen R. Covey', 'CAT005', 1989, 'Nhà xuất bản Trẻ', 6, 4, 'D-01-2'),
('B1015', '9780007442911', 'Đi tìm lẽ sống', 'Viktor E. Frankl', 'CAT203', 1946, 'Nhà xuất bản Trẻ', 5, 3, 'C-07-3');

-- Thêm dữ liệu nhân viên
INSERT INTO NhanVien (ID, HoTen, GioiTinh, ChucVu, Email, SoDienThoai, NgayVaoLam, TrangThai) VALUES
('NV001', 'Nguyễn Văn Hòa', 'Nam', 'Admin', 'nguyenhoa@gmail.com', '0901123456', '2023-01-10', 'Đang làm'),
('NV002', 'Trần Thị Mai', 'Nữ', 'Quản Lý', 'tranmaiqly@gmail.com', '0912234567', '2022-03-15', 'Đang làm'),
('NV003', 'Lê Minh Tuấn', 'Nam', 'Nhân Viên', 'letuan_nv@gmail.com', '0923345678', '2021-07-20', 'Đang làm'),
('NV004', 'Hoàng Đức Anh', 'Nam', 'Nhân Viên', 'hoangduca@gmail.com', '0934456789', '2020-10-05', 'Tạm nghỉ'),
('NV005', 'Vũ Thị Hồng', 'Nữ', 'Nhân Viên', 'vuthihong@gmail.com', '0945567890', '2023-05-12', 'Đang làm'),
('NV006', 'Phạm Quốc Bảo', 'Nam', 'Nhân Viên', 'phamquocbao@gmail.com', '0956678901', '2021-08-17', 'Tạm nghỉ'),
('NV007', 'Đặng Thúy Hằng', 'Nữ', 'Nhân Viên', 'dangthuyhang@gmail.com', '0967789012', '2022-02-10', 'Đang làm'),
('NV008', 'Bùi Văn Khánh', 'Nam', 'Nhân Viên', 'buivankhanh@gmail.com', '0978890123', '2020-11-25', 'Đang làm'),
('NV009', 'Ngô Thị Hạnh', 'Nữ', 'Nhân Viên', 'ngothihanh@gmail.com', '0989901234', '2019-06-30', 'Tạm nghỉ'),
('NV010', 'Đỗ Hoàng Sơn', 'Nam', 'Nhân Viên', 'dohoangson@gmail.com', '0991012345', '2023-09-05', 'Đang làm');

-- Thêm dữ liệu thành viên
INSERT INTO ThanhVien (MaThanhVien, HoTen, GioiTinh, SoDienThoai, Email, LoaiThanhVien, NgayDangKy, NgayHetHan, TrangThai) VALUES
('M0001', 'Nguyễn Văn An', 'Nam', '0987654321', 'nguyenvana@gmail.com', 'Sinh viên', '2023-01-01', '2025-01-01', 'Hoạt động'),
('M0002', 'Trần Thị Bích Ngọc', 'Nữ', '0912345678', 'bichngoc@gmail.com', 'Sinh viên', '2023-01-15', '2025-01-15', 'Hoạt động'),
('M0003', 'Lê Hoàng Nam', 'Nam', '0905678123', 'lehoangnam@gmail.com', 'Giảng viên', '2023-02-10', '2024-02-10', 'Hoạt động'),
('M0004', 'Phạm Thu Hương', 'Nữ', '0977234567', 'huongpham@gmail.com', 'Sinh viên', '2023-03-05', '2024-03-05', 'Hoạt động'),
('M0005', 'Hoàng Thị Lan', 'Nữ', '0921456789', 'hoanglan@gmail.com', 'Thường', '2023-03-20', '2025-03-20', 'Hoạt động'),
('M0006', 'Vũ Đức Thành', 'Nam', '0968234890', 'ducthanh123@gmail.com', 'Sinh viên', '2023-04-10', '2025-04-10', 'Hoạt động'),
('M0007', 'Bùi Thanh Mai', 'Nữ', '0945678234', 'thanhmai_bui@gmail.com', 'Giảng viên', '2023-05-15', '2025-05-15', 'Hoạt động'),
('M0008', 'Đỗ Quang Huy', 'Nam', '0982345678', 'huydo@gmail.com', 'Sinh viên', '2023-06-05', '2024-06-05', 'Hoạt động'),
('M0009', 'Nguyễn Thị Kim Anh', 'Nữ', '0909123456', 'kimanh99@gmail.com', 'Thường', '2023-06-20', '2025-06-20', 'Hết hạn'),
('M0010', 'Lý Văn Duy', 'Nam', '0915678901', 'lyvduy@gmail.com', 'Sinh viên', '2023-07-10', '2024-07-10', 'Hoạt động'),
('M0011', 'Nguyễn Minh Tú', 'Nam', '0911123456', 'minhtu@gmail.com', 'Sinh viên', '2023-08-10', '2024-08-10', 'Hoạt động'),
('M0012', 'Phạm Hoàng Yến', 'Nữ', '0988112233', 'hoangyen_pham@gmail.com', 'Sinh viên', '2023-09-05', '2025-09-05', 'Hoạt động'),
('M0013', 'Bùi Hữu Nghĩa', 'Nam', '0977556677', 'huunghia.bui@gmail.com', 'Giảng viên', '2023-10-15', '2025-10-15', 'Hoạt động'),
('M0014', 'Lê Hải Đăng', 'Nam', '0933445566', 'haidang_le@gmail.com', 'Thường', '2023-11-20', '2025-11-20', 'Hoạt động'),
('M0015', 'Trần Thu Trang', 'Nữ', '0966778899', 'trangtran@gmail.com', 'Sinh viên', '2023-12-01', '2024-12-01', 'Hoạt động');

-- thêm dữ liệu cho phiếu mượn
INSERT INTO PhieuMuon (MaThanhVien, NgayMuon, HanTra, NgayTraThucTe, TrangThai, MaSach, SoLuong) VALUES
('M0001', '2025-03-01', '2025-03-15', NULL, 'Đang mượn', 'B1001', 1),
('M0002', '2025-02-20', '2025-03-10', '2025-03-09', 'Đã trả', 'B1003', 1),
('M0003', '2025-03-05', '2025-03-19', NULL, 'Đang mượn', 'B1005', 2),
('M0004', '2025-03-08', '2025-03-22', NULL, 'Đang mượn', 'B1012', 1),
('M0005', '2025-02-28', '2025-03-14', NULL, 'Quá hạn', 'B1010', 1),
('M0006', '2025-02-25', '2025-03-11', '2025-03-10', 'Đã trả', 'B1007', 1),
('M0007', '2025-03-10', '2025-03-24', NULL, 'Đang mượn', 'B1009', 2),
('M0008', '2025-02-15', '2025-03-01', NULL, 'Quá hạn', 'B1011', 1),
('M0009', '2025-03-01', '2025-03-15', NULL, 'Đang mượn', 'B1004', 1),
('M0010', '2025-02-27', '2025-03-13', '2025-03-15', 'Đã trả', 'B1006', 1), -- Trả trễ 2 ngày
('M0011', '2025-03-02', '2025-03-16', NULL, 'Đang mượn', 'B1013', 1),
('M0012', '2025-03-06', '2025-03-20', NULL, 'Đang mượn', 'B1002', 1),
('M0013', '2025-03-09', '2025-03-23', NULL, 'Đang mượn', 'B1008', 1),
('M0014', '2025-02-22', '2025-03-08', '2025-03-12', 'Đã trả', 'B1014', 1), -- Trả trễ 4 ngày
('M0015', '2025-03-04', '2025-03-18', NULL, 'Đang mượn', 'B1015', 1);

UPDATE PhieuMuon
SET TrangThai = 'Quá hạn'
WHERE TrangThai = 'Đang mượn' AND HanTra < CURDATE();