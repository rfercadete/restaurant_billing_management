package com.restaurant.controller

import com.constants.CodeConstants
import com.utils.ServiceContext
import com.utils.SessionUtil
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class RestaurantManagementController {
    def restaurantManagementService
    def userManagementService
    def springSecurityService
    def commonUtilService

    /*-------------------------- START : Branch Management ---------------------------------*/
    @Secured(['ROLE_SUPER_ADMIN'])
    def branchManagement(){}

    @Secured(['ROLE_SUPER_ADMIN'])
    def newBranch(){
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)

        Map branchCreationStatusMap =   restaurantManagementService.newBranchCreation(params.branchName,params.address,
                params.contactNumber, sCtx.restaurantId)
        render branchCreationStatusMap as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def updateBranch(){
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)
        Map detailsToUpdate =   [name: params.branchName, address: params.address, contactNumber : params.contactNumber]
        Map branchUpdateStatusMap   =   restaurantManagementService.updateBranchDetails(params.branchId, sCtx.restaurantId, detailsToUpdate)
        render branchUpdateStatusMap as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def deleteBranch(){
        Map branchDeletionStatus    =   restaurantManagementService.deleteBranch(params.branchId)
        render branchDeletionStatus as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def fetchBranchDetails(){
        Map branchDetailsMap    =   [:]
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)
        List allBranchDetailsList   =   restaurantManagementService.branchDetails(sCtx.restaurantId)
        branchDetailsMap << [ data : allBranchDetailsList]
        render branchDetailsMap as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def fetchBranchNames(){
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)
        List branchNameList   =   restaurantManagementService.branchNameByRestaurantId(sCtx.restaurantId)
        render branchNameList as JSON
    }
    /*-------------------------- END : Branch Management ---------------------------------*/

    /*-------------------------- START : Restaurant User Management ----------------------*/

    @Secured(['ROLE_SUPER_ADMIN'])
    def userManagement(){}

    /**
     * New user creation
     * @return : userCreationStatusMap
     */
    @Secured(['ROLE_SUPER_ADMIN'])
    def createUser(){
        String branchId =   ""
        Map userCreationStatusMap   =   [:]
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)

        branchId =   commonUtilService.fetchBranchIdByNameAndRestaurantId(sCtx.restaurantId, params.branchName)
        if (branchId != ""){
            userCreationStatusMap    =   userManagementService.newUserCreation(params.userName, params.password,
                    params.firstName, params.lastName, params.contactNumber, CodeConstants.ROLE_ADMIN,
                    sCtx.restaurantId, branchId)
        }else {
            userCreationStatusMap << [status : false, message : "Invalid branch name"]
        }
        render userCreationStatusMap as JSON
    }

    /**
     * Updating user information
     * @return : userUpdateStatusMap
     */
    @Secured(['ROLE_SUPER_ADMIN'])
    def updateUserInformation(){
        println "params :"+params
        String branchId = ""
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)

        //building map for user details to update
        Map detailsToUpdate =  [firstName : params.firstName, lastName : params.lastName, contactNumber: params.contactNumber]
        if (params.branchName != ""){
            branchId =   commonUtilService.fetchBranchIdByNameAndRestaurantId(sCtx.restaurantId, params.branchName)
            if (branchId != ""){
                detailsToUpdate << [branchId : branchId]
            }
        }

        if (params.password != ""){
            detailsToUpdate << [password : params.password]
        }

        Map  userUpdateStatusMap = userManagementService.updateUserInformation(params.userId, detailsToUpdate)
        render userUpdateStatusMap as JSON
    }

    /**
     * Deleting the user
     * @return : userDeletionStatusMap
     */
    @Secured(['ROLE_SUPER_ADMIN'])
    def deleteUser(){
        Map userDeletionStatusMap   =   userManagementService.deleteUser(params.userId)
        render userDeletionStatusMap as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def fetchUserDetails(){
        Map userDetailsMap      =   [:]
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)

        List userDetailsList    =   userManagementService.fetchAllUsersByRestaurantId(sCtx.restaurantId)
        userDetailsMap << [data : userDetailsList]
        render userDetailsMap as JSON
    }
    /*-------------------------- END : Restaurant User Management -----------------------*/

    /*-------------------------- START : Menu Management -----------------------*/

    @Secured(['ROLE_SUPER_ADMIN'])
    def menuManagement(){}

    @Secured(['ROLE_SUPER_ADMIN'])
    def branchWiseMenuManagement(){}

    @Secured(['ROLE_SUPER_ADMIN'])
    def newMenu(){
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)

        Map menuCreationStatusMap   = restaurantManagementService.createMenu(sCtx.restaurantId, params.menuName)
        render menuCreationStatusMap as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def updateMenu(){
        Map menuUpdateStatusMap   = restaurantManagementService.updateMenu(params.menuId, params.menuName)
        render menuUpdateStatusMap as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def deleteMenu(){
        Map menuDeletionStatusMap   = restaurantManagementService.deleteMenu(params.menuId)
        render menuDeletionStatusMap as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def fetchMenu(){
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)

        Map  menuDetailMap  =   [:]

        List menuList   =   restaurantManagementService.fetchMenuList(sCtx.restaurantId)
        menuDetailMap   <<  [data : menuList]
        render menuDetailMap as JSON
    }

    @Secured(['ROLE_SUPER_ADMIN'])
    def fetchBranchWiseMenuDetails(){
        println "inside--"+params
        Map branchWiseMenuDetailsMap    =   [:]
        ServiceContext sCtx = SessionUtil.getServiceContext(request, springSecurityService, userManagementService)

        String branchId = commonUtilService.fetchBranchIdByNameAndRestaurantId(sCtx.restaurantId, params.branchName)
        List branchWiseMenuDetails  = restaurantManagementService.fetchBranchWiseMenuDetails(branchId)
        branchWiseMenuDetailsMap << [data: branchWiseMenuDetails]
        println "inside--" +branchWiseMenuDetailsMap

        render branchWiseMenuDetailsMap as JSON
    }

    /*-------------------------- END : Menu Management -----------------------*/
}