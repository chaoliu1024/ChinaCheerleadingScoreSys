USE [score_sys]
GO
/****** Object:  Table [dbo].[web_json]    Script Date: 11/22/2014 17:41:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[web_json](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[web_id] [int] NULL,
	[match_category] [nvarchar](100) NOT NULL,
	[match_units] [nvarchar](100) NOT NULL,
	[team_name] [nvarchar](100) NOT NULL,
	[match_name] [nvarchar](100) NOT NULL,
	[final_preliminary] [bit] NOT NULL,
	[sort_flag] [bit] NOT NULL,
	[player_name] [nvarchar](500) NULL,
	[coach_name] [nvarchar](500) NULL,
	[tb_name] [nvarchar](500) NULL,
 CONSTRAINT [PK_web_JSON] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'web_json', @level2type=N'COLUMN',@level2name=N'id'
GO
/****** Object:  UserDefinedFunction [dbo].[getTotalScore]    Script Date: 11/22/2014 17:41:38 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date, ,>
-- Description:	<Description, ,>
-- =============================================
CREATE FUNCTION [dbo].[getTotalScore]
(
	-- Add the parameters for the function here
	@score1 decimal(6, 1),
	@score2 decimal(6, 1),
    @score3 decimal(6, 1),
	@score4 decimal(6, 1),
    @score5 decimal(6, 1),
	@score6 decimal(6, 1),
    @score7 decimal(6, 1),
	@score8 decimal(6, 1),
    @score9 decimal(6, 1),
    @score10 decimal(6, 1),
	@sub_score decimal(6, 1)
)
RETURNS decimal(6, 1)  ---- 返回值类型
AS
BEGIN
	-- Declare the return variable here
	
	DECLARE @Result decimal (6, 1)
	set @Result=0;

	----若分1不为空
	IF(@score1 is not null)
	Begin
		set @Result = @Result+@score1;
	End
	----若分2不为空
	IF(@score2 is not null)
	Begin
		set @Result = @Result+@score2;
	End
   ----若分3不为空
	IF(@score3 is not null)
	Begin
		set @Result = @Result+@score3;
	End
    ----若分4不为空
	IF(@score4 is not null)
	Begin
		set @Result = @Result+@score4;
	End
     ----若分5不为空
	IF(@score5 is not null)
	Begin
		set @Result = @Result+@score5;
	End
    ----若分6不为空
	IF(@score6 is not null)
	Begin
		set @Result = @Result+@score6;
	End
    ----若分7不为空
	IF(@score7 is not null)
	Begin
		set @Result = @Result+@score7;
	End
	----若分8不为空
	IF(@score8 is not null)
	Begin
		set @Result = @Result+@score8;
	End
	----若分9不为空
	IF(@score9 is not null)
	Begin
		set @Result = @Result+@score9;
	End
	----若分9不为空
	IF(@score10 is not null)
	Begin
		set @Result = @Result+@score10;
	End
    ----若减分不为空
	IF(@sub_score is not null)
	Begin
		set @Result = @Result-@sub_score;
	End
	RETURN @Result

END
GO
/****** Object:  Table [dbo].[config]    Script Date: 11/22/2014 17:41:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[config](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](30) NULL,
	[role] [nvarchar](30) NOT NULL,
	[location] [nvarchar](30) NULL,
 CONSTRAINT [PK_config] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[config] ON
INSERT [dbo].[config] ([id], [name], [role], [location]) VALUES (1, N'蔡秋月', N'intercessor', N'辽宁 沈阳')
INSERT [dbo].[config] ([id], [name], [role], [location]) VALUES (2, N'', N'viceReferee', N'辽宁 沈阳')
INSERT [dbo].[config] ([id], [name], [role], [location]) VALUES (3, N'金晓阳', N'refree', N'辽宁 沈阳')
SET IDENTITY_INSERT [dbo].[config] OFF
/****** Object:  Table [dbo].[roles]    Script Date: 11/22/2014 17:41:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[roles](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[role_name] [varchar](50) NOT NULL,
	[role_value] [varchar](50) NOT NULL,
	[login_flag] [bit] NOT NULL,
	[part_in] [bit] NULL,
 CONSTRAINT [PK_roles] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
SET IDENTITY_INSERT [dbo].[roles] ON
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (5, N'打分裁判1-01', N'judge1-01', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (6, N'打分裁判1-02', N'judge1-02', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (7, N'打分裁判1-03', N'judge1-03', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (8, N'打分裁判1-04', N'judge1-04', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (9, N'打分裁判1-05', N'judge1-05', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (10, N'打分裁判1-06', N'judge1-06', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (11, N'打分裁判1-07', N'judge1-07', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (12, N'打分裁判1-08', N'judge1-08', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (13, N'打分裁判1-09', N'judge1-09', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (14, N'打分裁判1-10', N'judge1-10', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (17, N'裁判长01', N'chiefJudge01', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (19, N'裁判长02', N'chiefJudge02', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (20, N'裁判长03', N'chiefJudge03', 0, 1)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (33, N'打分裁判2-01', N'judge2-01', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (36, N'打分裁判2-02', N'judge2-02', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (37, N'打分裁判2-03', N'judge2-03', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (38, N'打分裁判2-04', N'judge2-04', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (39, N'打分裁判2-05', N'judge2-05', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (40, N'打分裁判2-06', N'judge2-06', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (41, N'打分裁判2-07', N'judge2-07', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (42, N'打分裁判2-08', N'judge2-08', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (43, N'打分裁判2-09', N'judge2-09', 0, 0)
INSERT [dbo].[roles] ([id], [role_name], [role_value], [login_flag], [part_in]) VALUES (44, N'打分裁判2-10', N'judge2-10', 0, 0)
SET IDENTITY_INSERT [dbo].[roles] OFF
/****** Object:  Table [dbo].[match_order]    Script Date: 11/22/2014 17:41:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[match_order](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[web_json_id] [int] NOT NULL,
	[match_num] [int] NOT NULL,
	[match_order] [int] NOT NULL,
	[match_category] [nvarchar](100) NOT NULL,
	[team_name] [nvarchar](100) NOT NULL,
	[final_preliminary] [bit] NOT NULL,
	[match_name] [nvarchar](100) NOT NULL,
	[unit_status] [int] NOT NULL,
	[player_name] [nvarchar](500) NULL,
 CONSTRAINT [PK_match_order] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[score]    Script Date: 11/22/2014 17:41:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[score](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[team_id] [int] NOT NULL,
	[score1] [decimal](6, 1) NOT NULL,
	[score2] [decimal](6, 1) NOT NULL,
	[score3] [decimal](6, 1) NOT NULL,
	[score4] [decimal](6, 1) NOT NULL,
	[score5] [decimal](6, 1) NOT NULL,
	[score6] [decimal](6, 1) NOT NULL,
	[score7] [decimal](6, 1) NOT NULL,
	[score8] [decimal](6, 1) NOT NULL,
	[score9] [decimal](6, 1) NOT NULL,
	[score10] [decimal](6, 1) NULL,
	[referee_sub_score] [decimal](6, 1) NULL,
	[total_score] [decimal](6, 2) NOT NULL,
	[score_error1] [decimal](6, 2) NULL,
	[score_error2] [decimal](6, 2) NULL,
	[score_error3] [decimal](6, 2) NULL,
	[score_error4] [decimal](6, 2) NULL,
	[score_error5] [decimal](6, 2) NULL,
	[score_error6] [decimal](6, 2) NULL,
	[score_error7] [decimal](6, 2) NULL,
	[score_error8] [decimal](6, 2) NULL,
	[score_error9] [decimal](6, 2) NULL,
	[score_error10] [decimal](6, 2) NULL,
 CONSTRAINT [PK_score] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  ForeignKey [FK_match_order_web_json]    Script Date: 11/22/2014 17:41:36 ******/
ALTER TABLE [dbo].[match_order]  WITH CHECK ADD  CONSTRAINT [FK_match_order_web_json] FOREIGN KEY([web_json_id])
REFERENCES [dbo].[web_json] ([id])
GO
ALTER TABLE [dbo].[match_order] CHECK CONSTRAINT [FK_match_order_web_json]
GO
/****** Object:  ForeignKey [FK_score_match_order]    Script Date: 11/22/2014 17:41:36 ******/
ALTER TABLE [dbo].[score]  WITH CHECK ADD  CONSTRAINT [FK_score_match_order] FOREIGN KEY([team_id])
REFERENCES [dbo].[match_order] ([id])
GO
ALTER TABLE [dbo].[score] CHECK CONSTRAINT [FK_score_match_order]
GO
