package com.easecurity

import com.easecurity.admin.auth.UserAdmin

class BootStrap {
//	AnimalService animalService
//	WeighingService weighingService
//	PhotoService photoService
//	@Autowired
//	LdapUserService ldapUserService
//	@Autowired
//	LdapUserSyncService ldapUserSyncService

static Map<String, String> BANKCARD =
            ['A1': '10', 'A2': '84', 'A3': '93', 'A4': '12', 'A5': '92',
             'A6': '58', 'A7': '38', 'A8': '28', 'A9': '36', 'A10': '02',
             'B1': '99', 'B2': '29', 'B3': '10', 'B4': '23', 'B5': '33',
             'B6': '47', 'B7': '58', 'B8': '39', 'B9': '34', 'B10': '18',
             'C1': '28', 'C2': '05', 'C3': '29', 'C4': '03', 'C5': '94',
             'C6': '14', 'C7': '41', 'C8': '33', 'C9': '11', 'C10': '39',
             'D1': '01', 'D2': '49', 'D3': '39', 'D4': '79', 'D5': '53',
             'D6': '38', 'D7': '17', 'D8': '88', 'D9': '70', 'D10': '12'
            ]

    def init = { servletContext ->
//        UserAdmin user = new UserAdmin(user: 'admin', password: 'admin')
//        user.save(failOnError: true)
//        UserAdmin u = new UserAdmin(user: 'sherlock', password: 'elementary')
//            BANKCARD.each { k, v ->
//                u.addToCoordinates(new SecurityCoordinate(position: k, value: v, user: u))
//            }
//        u.save(failOnError: true)
//
//        UserAdmin u2 = new UserAdmin(user: 'user', password: 'user')
//            BANKCARD.each { k, v ->
//                u2.addToCoordinates(new SecurityCoordinate(position: k, value: v, user: u2))
//            }
//        u2.save(failOnError: true)
//        Role role = new Role(authority: 'ROLE_USER')
//        role.save(failOnError: true)
//        new UserRole(user: user, role: role).save()
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
