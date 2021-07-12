package com.easecurity

class BootStrap {
//	AnimalService animalService
//	WeighingService weighingService
//	PhotoService photoService
//	@Autowired
//	LdapUserService ldapUserService
//	@Autowired
//	LdapUserSyncService ldapUserSyncService
	
    def init = { servletContext ->
//		if (Environment.current == Environment.DEVELOPMENT) {
//			if (User.count()==0) {
//				def adminRole = Role.findByAuthority("ROLE_ADMIN")?:new Role(authority: 'ROLE_ADMIN').save()
//				def userRole = Role.findByAuthority("ROLE_USER")?:new Role(authority: 'ROLE_USER').save()
//				def userPM = Role.findByAuthority("ROLE_PM")?:new Role(authority: 'ROLE_PM').save()
//				def userPMO = Role.findByAuthority("ROLE_PMO")?:new Role(authority: 'ROLE_PMO').save()
//
//				def user = new User(username: 'user', password: 'user', firstname:"Gero", lastname:"Klinkmann", email:"user@gmail.com")
//				user.save()
//
//				def admin = new User(username: 'admin', password: 'admin', firstname:"Ad", lastname:"Min", email:"admin@gmail.com")
//				admin.save()
//
////				('test', 'test', '');
//				(1..50).each{
//					ldapUserSyncService.syncUser('liulf'+it, 'test', '')
//				}
////				ldapUserSyncService.syncUser('liulf77', 'test', '')
//				ldapUserSyncService.syncUserNow([uid:'liulf77', username:'test'])
//				ldapUserSyncService.syncUserNow([uid:'test', username:'test'])
//				def test = LdapUser.findByUid('test')
//				LdapUserRole.create(test, adminRole)
//				LdapUserRole.create(test, userPM)
//				LdapUserRole.create(test, userPMO)
//
//				UserRole.create(user, userRole)
//				UserRole.create(admin, userRole)
//				UserRole.create(admin, adminRole)
//			}
//
//			if (Project.count()==0) {
//				def liulf = LdapUser.findByUid('liulf77')
//				def lf = new Staff(uid: 'liulf77', user: liulf)
//				lf.save()
//				def test = LdapUser.findByUid('test')
//				def tt = new Staff(uid: 'test', user: test)
//				tt.save()
//				def sf = new StaffCost(staff: tt, year: 2020)
//				sf.save()
//				def project = new Project(code: 'SK2', name: '2020司库研发', fullName: '2020年中国联通财务公司司库系统研发迭代项目',
//						outBudget: 8900000, inBudget: 2000000, fIntegral: 1500, pm: tt)
//				project.save()
//				def pjxq = new ProjectJira(project: project, code: 'SK2_XQ', name: '2020司库研发需求')
//				pjxq.save()
//				def pjkf = new ProjectJira(project: project, code: 'SK2_KF', name: '2020司库研发开发')
//				pjkf.save()
//				def pjbug = new ProjectJira(project: project, code: 'SK2_BUG', name: '2020司库研发BUG')
//				pjbug.save()
//				def pjsh = new ProjectJira(project: project, code: 'SK2_SH', name: '2020司库研发售后')
//				pjsh.save()
//				def pr = new ProjectRole(code: 'LD', name: '开发经理', bIntegral: 0.4)
//				pr.save()
//				def spr = new StaffProjectRole(project: project, staff: tt, projectRole: pr, bIntegral: 0.35)
//				spr.save()
//				def wr = WeeklyReport.create(tt, new Date())
//				new DailyReport(jwCode:'SK2_KF1',jwName:'开发头寸管理',taskContent:'xxx类编码',taskTime:0.5f,phase: ReportEnum.ProjectPhase.编码阶段,
//						process: ReportEnum.ProjectProcess.程序编码,projectJira:pjkf,staff:tt,staffProjectRole:spr,weeklyReport:wr).save(deepValidate:false)
//			}
//
//			if (animalService.count()==0) {
//
//				// animals
//				(1..20).each{
//					Animal animal=new Animal(name: 'A'+it, cageNr: it.toInteger(),arrived:new java.sql.Date(new Date().time), approbation:'app1'
//					,gender:'f',species:'c', createdBy:"juhu", lastUpdatedBy:"juhu")
//				animalService.save(animal)
//				}
//
//				// weighings
//				Animal animal=animalService.get(1)
//				(1..10).each{
//					Weighing weighing=new Weighing(weighted: new java.sql.Date(new Date().time)
//					,createdBy:"juhu", lastUpdatedBy:"juhu",weight:100+it.toInteger()*2,animal:animal)
//					weighingService.save(weighing)
//				}
//
//				// photos
//				["a","b"].each{
//					Photo photo=new Photo(created: new java.sql.Date(new Date().time)
//					,createdBy:"juhu", name:"A4_${it}.JPG",animal:animal)
//					photoService.save(photo)
//				}
//
//			}
//		}
//
//		// TODO 执行所有service、domain类的bootStrapInit方法，参见BroToolkitGrailsPlugin.groovy
//		ldapUserSyncService.bootStrapInit()
    }
	
    def destroy = {
    }
}
